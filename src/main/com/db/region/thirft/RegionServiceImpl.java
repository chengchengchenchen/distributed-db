package com.db.region.thirft;

import com.db.RPC.model.*;
import com.db.RPC.service.RegionService;
import com.db.common.enums.DataServerStateEnum;
import com.db.common.enums.SqlQueryEnum;
import com.db.region.service.RegionConfig;
import org.apache.thrift.TException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class RegionServiceImpl implements RegionService.Iface {

    /**
     * Client根据表格名查询表格实际数据
     *
     * @param req
     */
    @Override
    public QueryTableDataResponse queryTableData(QueryTableDataRequest req) throws org.apache.thrift.TException {
        Statement stat = RegionConfig.stat;
        QueryTableDataResponse response = new QueryTableDataResponse();
        ResultSetData result = new ResultSetData();
        try {
            ResultSet rs = stat.executeQuery(req.getSql());
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(md.getColumnName(i));
            }
            result.setColumnNames(columnNames);
            List<List<String>> rows = new ArrayList<>();
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                rows.add(row);
            }
            result.setRows(rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setResultSetData(result);
        return response;
    }

    /**
     * Client修改表格数据
     *
     * @param req
     */
    @Override
    public ChangeTableDataResponse changeTableData(ChangeTableDataRequest req) throws org.apache.thrift.TException {
        Statement stat = RegionConfig.stat;
        ChangeTableDataResponse response = new ChangeTableDataResponse();
        int result = 0;
        try {
            result = stat.executeUpdate(req.getSql());
            if (req.getType() == SqlQueryEnum.CREATE_INDEX.ordinal() ||
                    req.getType() == SqlQueryEnum.CREATE_TABLE.ordinal() ||
                    req.getType() == SqlQueryEnum.DROP_INDEX.ordinal() ||
                    req.getType() == SqlQueryEnum.DROP_TABLE.ordinal()) {   //传string在副本机上执行
                if (RegionConfig.state == DataServerStateEnum.PRIMARY) {
                    //TODO:开启主件机和副本机的socket链接
                    //changeTableData(req);
                }
            } else {                    //传sql文件覆盖
                System.out.println(System.getProperty("user.dir"));
                try {
                    Runtime rt = Runtime.getRuntime();
                    StringBuilder str = new StringBuilder();
                    str.append("mysqldump -h localhost -u ").append(RegionConfig.username).append(" -p").append(RegionConfig.password).append(" distributed_db ").append(req.getName()).append(">").append(System.getProperty("user.dir")).append("\\").append(req.getName()).append(".sql");
                    rt.exec(str.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //TODO:开启主件机和副本机的socket链接
                //com.db.RPC.model.ExecTableCopyResponse(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setResult(result);
        return response;
    }

    /**
     * Master告知Region状态变化
     *
     * @param req
     */
    @Override
    public NotifyStateResponse notifyStateChange(NotifyStateRequest req) throws org.apache.thrift.TException {
        return null;
    }

    /**
     * Region对table进行全量复制
     *
     * @param req
     */
    @Override
    public ExecTableCopyResponse execTableCopy(ExecTableCopyRequest req) throws org.apache.thrift.TException {
        ExecTableCopyResponse response = new ExecTableCopyResponse();
        //把filedata写到项目文件夹下
        try (FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir"));
             FileChannel channel = fos.getChannel()) {
            channel.write(ByteBuffer.wrap(req.fileData.getFileContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Runtime rt = Runtime.getRuntime();
            StringBuilder str = new StringBuilder();
            str.append("mysql -h localhost -u ").append(RegionConfig.username).append(" -p").append(RegionConfig.password).append(" distributed_db ").append("<").append(System.getProperty("user.dir")).append("\\").append(req.getName()).append(".sql");
            rt.exec(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public StartSchemaCopyResponse startSchemaCopy(StartSchemaCopyRequest req) throws TException {
        return null;
    }

    @Override
    public ExecSchemaCopyResponse execSchemaCopy(ExecSchemaCopyRequest req) throws TException {
        return null;
    }
}
