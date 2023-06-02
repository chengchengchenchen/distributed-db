package com.db.region.thrift;

import com.db.RPC.model.*;
import com.db.RPC.service.MasterService;
import com.db.RPC.service.RegionService;
import com.db.common.constant.MasterConstant;
import com.db.common.enums.DataServerStateEnum;
import com.db.common.enums.SqlQueryEnum;
import com.db.common.model.DataServer;
import com.db.region.service.RegionConfig;
import org.apache.thrift.TException;

import java.io.FileInputStream;
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

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import com.db.common.utils.rpc.RpcResult;

//TODO:notifiedTableMetaChange触发时间,FileData的具体数据可能不准确
@Slf4j
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
            String message = e.getMessage();
            response.setBaseResp(new BaseResp(message,-1));
            response.setResultSetData(result);
            return response;
        }
        response.setResultSetData(result);
        response.setBaseResp(RpcResult.successResp());
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
        DataServer dataServer = DataServer.builder().hostUrl(RegionConfig.dualURL).state(DataServerStateEnum.COPY).build();
        dataServer.parseHostUrl();
        String targetIp = dataServer.ip;
        Integer targetPort = dataServer.port;
        int result = 0;
        try {
            result = stat.executeUpdate(req.getSql());
            log.warn("执行了sql语句:{}",req.getSql());
            if (req.getType() == SqlQueryEnum.CREATE_INDEX.ordinal() ||
                    req.getType() == SqlQueryEnum.CREATE_TABLE.ordinal() ||
                    req.getType() == SqlQueryEnum.DROP_INDEX.ordinal() ||
                    req.getType() == SqlQueryEnum.DROP_TABLE.ordinal()) {   //传string在副本机上执行
                if (RegionConfig.state == DataServerStateEnum.PRIMARY) {
                    //TODO: 开启主件机和副本机的socket链接
                    //changeTableData(req);
                    log.warn("一般情况主从复制");
                    ChangeTableDataRequest changeTableDataRequest = new ChangeTableDataRequest()
                            .setName(req.getName())
                            .setSql(req.getSql())
                            .setType(req.getType())
                            .setBase(new Base()
                                    //.setCaller("SERVER"+ ServerConstant.getHostname())
                                    .setCaller(RegionConfig.hostName)
                                    .setReceiver(RegionConfig.dualName));
                    RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class,targetIp,targetPort);
                    regionServerClient.changeTableData(changeTableDataRequest);
                } else {

                }

            } else {
                if(RegionConfig.state == DataServerStateEnum.PRIMARY){
                    //传sql文件覆盖
                    System.out.println(System.getProperty("user.dir"));
                    try {
                        Runtime rt = Runtime.getRuntime();
                        StringBuilder str = new StringBuilder();
                        str.append("cmd /k mysqldump -h localhost -u ").append(RegionConfig.username).append(" -p").append(RegionConfig.password).append(" ").append(RegionConfig.dbName).append(" ").append(req.getName()).append(" > ").append(System.getProperty("user.dir")).append("\\").append(req.getName()).append(".sql");
                        log.warn("str is:{}",str);
                        rt.exec(str.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //gpt写的
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e){
                        log.warn("sleep 异常");
                    }
                    String fileName = req.getName() + ".sql";
                    String filePath = fileName;
                    File file = new File(filePath);
                    log.warn("filePath:{}",filePath);
                    if(file == null){
                        log.warn("file is null!!!");
                    }
                    // 创建一个字节缓冲区，大小为文件长度
                    ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
                    // 创建一个文件输入流，读取文件内容到缓冲区
                    try (FileInputStream fis = new FileInputStream(file);
                         FileChannel channel = fis.getChannel()) {
                        channel.read(buffer);
                        buffer.flip();
                    }
                    catch (IOException e){
                        log.warn("读取表失败");
                    }
                    // 创建一个文件数据对象，设置文件名和文件内容
                    FileData fileData = new FileData();
                    fileData.setFileName(fileName);
                    fileData.setFileContent(buffer);
                    //TODO: 开启主件机和副本机的socket链接
                    //ExecTableCopyResponse(req);
                    log.warn("特殊情况主从复制");
                    ExecTableCopyRequest execTableCopyRequest = new ExecTableCopyRequest()
                            .setName(req.getName())
                            .setFileData(fileData)
                            .setBase(new Base()
                                    .setCaller(RegionConfig.hostName)
                                    .setReceiver(RegionConfig.dualName));
                    RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class,targetIp,targetPort);
                    regionServerClient.execTableCopy(execTableCopyRequest);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            String message = e.getMessage();
            response.setBaseResp(new BaseResp(message,-1));
            response.setResult(result);
            return response;
        }
        //增删table时向master修改元数据
        if(req.getType() == SqlQueryEnum.CREATE_TABLE.ordinal() || req.getType() == SqlQueryEnum.DROP_TABLE.ordinal()){
            NotifyTableMetaChangeRequest notifyTableMetaChangeRequest = new NotifyTableMetaChangeRequest()
                    .setName(req.getName())
                    .setType(req.getType())
                    .setURL(RegionConfig.hostURL)
                    .setRegionName(RegionConfig.hostName)
                    .setBase(new Base()
                            .setCaller(RegionConfig.hostName)
                            .setReceiver(MasterConstant.MASTER_HOST_NAME));
            MasterServerClient masterServerClient = new MasterServerClient(MasterService.Client.class,MasterConstant.MASTER_SERVER_IP,MasterConstant.MASTER_SERVER_PORT);
            masterServerClient.notifyTableMetaChange(notifyTableMetaChangeRequest);
        }
        response.setResult(result);
        response.setBaseResp(RpcResult.successResp());
        return response;
    }

    /**
     * Master告知Region状态变化
     *
     * @param req
     */
    @Override
    public NotifyStateResponse notifyStateChange(NotifyStateRequest req) throws org.apache.thrift.TException {
        DataServerStateEnum nowState = DataServerStateEnum.fromCode(req.getStateCode());
        log.warn("接收到Master通知，{}状态变更为{}，对偶机为{}:{}", RegionConfig.hostName, nowState, req.getDualServerName(), req.getDualServerUrl());
        RegionConfig.state = nowState;
        RegionConfig.dualName= req.getDualServerName();;
        RegionConfig.dualURL = req.getDualServerUrl();
        return new NotifyStateResponse()
                .setBaseResp(RpcResult.successResp());
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
        try (FileOutputStream fos = new FileOutputStream(req.getName() + ".sql");
             FileChannel channel = fos.getChannel()) {
            channel.write(ByteBuffer.wrap(req.fileData.getFileContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Runtime rt = Runtime.getRuntime();
            StringBuilder str = new StringBuilder();
            str.append("cmd /k mysql -h localhost -u ").append(RegionConfig.username).append(" -p").append(RegionConfig.password).append(" ").append(RegionConfig.dbName).append(" < ").append(System.getProperty("user.dir")).append("\\").append(req.getName()).append(".sql");
            rt.exec(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setBaseResp(RpcResult.successResp());
        return response;
    }

    @Override
    public StartSchemaCopyResponse startSchemaCopy(StartSchemaCopyRequest req) throws TException {
        StartSchemaCopyResponse res = new StartSchemaCopyResponse();
        try {
            Runtime rt = Runtime.getRuntime();
            StringBuilder str = new StringBuilder();
            str.append("cmd /k mysqldump -h localhost -u ").append(RegionConfig.username).append(" -p").append(RegionConfig.password).append(" ").append(RegionConfig.dbName).append(" > ").append(System.getProperty("user.dir")).append("\\").append(RegionConfig.dbName).append(".sql");
            System.out.println(str);
            rt.exec(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO:创建当前region向dual的socket链接
        //初始化ExecSchemaCopyRequest
        //execSchemaCopy(req);
        //gpt写的
        try {
            Thread.sleep(2000);
        } catch (Exception e){
            log.warn("sleep 异常");
        }
        String FileName = RegionConfig.dbName + ".sql";
        String FilePath = FileName;
        File file = new File(FilePath);

        // 创建一个字节缓冲区，大小为文件长度
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        // 创建一个文件输入流，读取文件内容到缓冲区
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel()) {
            channel.read(buffer);
            buffer.flip();
        }
        catch (IOException e){
            log.warn("读取表失败");
        }
        // 创建一个文件数据对象，设置文件名和文件内容
        FileData fileData = new FileData();
        fileData.setFileName(FileName);
        fileData.setFileContent(buffer);
        log.warn("schema复制");
        ExecSchemaCopyRequest execSchemaCopyRequest = new ExecSchemaCopyRequest()
                .setName(FileName)
                .setFileData(fileData)
                .setBase(new Base()
                        .setCaller(RegionConfig.hostName)
                        .setReceiver(RegionConfig.dualName));
        DataServer dataServer = DataServer.builder().hostUrl(RegionConfig.dualURL).build();
        dataServer.parseHostUrl();
        String targetIp = dataServer.ip;
        Integer targetPort = dataServer.port;
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class,targetIp,targetPort);
        regionServerClient.execSchemaCopy(execSchemaCopyRequest);
        res.setBaseResp(RpcResult.successResp());
        return res;
    }

    @Override
    public ExecSchemaCopyResponse execSchemaCopy(ExecSchemaCopyRequest req) throws TException {
        ExecSchemaCopyResponse res = new ExecSchemaCopyResponse();
        try (FileOutputStream fos = new FileOutputStream(RegionConfig.dbName + ".sql");
             FileChannel channel = fos.getChannel()) {
            channel.write(ByteBuffer.wrap(req.fileData.getFileContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Runtime rt = Runtime.getRuntime();
            StringBuilder str = new StringBuilder();
            str.append("cmd /k mysql -h localhost -u ").append(RegionConfig.username).append(" -p").append(RegionConfig.password).append(" ").append(RegionConfig.dbName).append(" < ").append(System.getProperty("user.dir")).append("\\").append(RegionConfig.dbName).append(".sql");
            System.out.println(str);
            rt.exec(str.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.setBaseResp(RpcResult.successResp());

        return res;
    }
}
