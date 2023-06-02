package com.db.common.utils.rpc;

import com.db.RPC.model.BaseResp;
import com.db.common.enums.RpcResultCodeEnum;

/**
 * @Description: 一些通用函数，用于返回RPC结果
 * @author: csc
 * @date: 2023/05/26
 */
public class RpcResult {

    public static BaseResp failResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.FAIL.getCode())
                .setDesc(RpcResultCodeEnum.FAIL.getDesc());
    }

    public static BaseResp successResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.SUCCESS.getCode())
                .setDesc(RpcResultCodeEnum.SUCCESS.getDesc());
    }

    public static BaseResp notFoundResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.NOT_FOUND.getCode())
                .setDesc(RpcResultCodeEnum.NOT_FOUND.getDesc());
    }

    public static BaseResp hasExistedResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.HAS_EXISTED.getCode())
                .setDesc(RpcResultCodeEnum.HAS_EXISTED.getDesc());
    }


}
