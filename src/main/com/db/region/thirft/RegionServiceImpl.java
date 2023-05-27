package com.db.region.thirft;

import com.db.RPC.model.*;
import com.db.RPC.service.RegionService;
import com.db.common.enums.DataServerStateEnum;
import com.db.region.service.JDBC;
import org.hamcrest.Condition;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
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
        Statement stat = JDBC.stat;
        QueryTableDataResponse response = new QueryTableDataResponse();
        ResultSetData result = new ResultSetData();
        try{
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
        }catch (SQLException e){
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
        Statement stat = JDBC.stat;
        ChangeTableDataResponse response = new ChangeTableDataResponse();
        int result = 0;
        try {
            result = stat.executeUpdate(req.getSql());
            if (req.getType() <= 4) {   //传string在副本机上执行
                if (JDBC.state == DataServerStateEnum.PRIMARY) {
                    //TODO: 开启主件机和副本机的socket链接
                    //changeTableData(req);
                }
            } else {                    //传sql文件覆盖
                System.out.println(System.getProperty("user.dir"));
                try {
                    Runtime rt = Runtime.getRuntime();
                    StringBuilder str = new StringBuilder();
                    str.append("mysqldump -h localhost -u ").append(JDBC.username).append(" -p").append(JDBC.password).append(" distributed_db ").append(req.getName()).append(">").append(System.getProperty("user.dir")).append("\\").append(req.getName()).append(".sql");
                    rt.exec(str.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //TODO: 开启主件机和副本机的socket链接
                //ExecTableCopyResponse(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setResult(result);
        return  response;
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
            str.append("mysql -h localhost -u ").append(JDBC.username).append(" -p").append(JDBC.password).append(" distributed_db ").append("<").append(System.getProperty("user.dir")).append("\\").append(req.getName()).append(".sql");
            rt.exec(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
