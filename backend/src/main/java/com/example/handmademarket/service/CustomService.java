package com.example.handmademarket.service;

import com.example.handmademarket.dto.CustomRequest;
import com.example.handmademarket.util.ResponseResult;

public interface CustomService {

    /** 消费者：提交定制需求 */
    ResponseResult submitCustom(String username, CustomRequest request);

    /** 消费者：查看我的定制需求列表 */
    ResponseResult getMyCustoms(String username);

    /** 消费者：取消定制需求 */
    ResponseResult cancelCustom(String username, Integer customId);

    /** 消费者：确认定制完成（创作者交付后） */
    ResponseResult confirmCustom(String username, Integer customId, String deliveryAddress);

    /** 创作者：查看可接单的定制需求（待匹配+与自己品类匹配） */
    ResponseResult getAvailableCustoms(String username);

    /** 创作者：查看我接的定制单 */
    ResponseResult getMyAcceptedCustoms(String username);

    /** 创作者：接受定制需求 */
    ResponseResult acceptCustom(String username, Integer customId, java.math.BigDecimal quotedPrice);

    /** 创作者：拒绝定制需求 */
    ResponseResult rejectCustom(String username, Integer customId, String reason);

    /** 创作者：交付定制作品 */
    ResponseResult deliverCustom(String username, Integer customId, String deliverContent);

    /** 查看定制需求详情 */
    ResponseResult getCustomDetail(Integer customId);

    /** 管理员：查看所有定制需求 */
    ResponseResult getAllCustoms();
}
