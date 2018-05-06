package com.yibuwulianwang.handle;

public class Code {
    public static final int HTTP_API_NULL = 500 ;//HTTP API 请求为空
    public static final int HTTP_API_OK = 200 ;//HTTP API 请求成功
    public static final int HTTP_API_EXP = 400 ;//HTTP API 请求异常
    
    public static final int RESULT_CODE_SUCCESS = 200 ;//OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
    public static final int RESULT_CODE_CREATED = 201 ;//CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
    public static final int RESULT_CODE_ACCEPTED = 202 ;//Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
    public static final int RESULT_CODE_NO_CONTENT = 204 ;//NO CONTENT - [DELETE]：用户删除数据成功。
    public static final int RESULT_CODE_INVALID_REQUEST = 400 ;//INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
    public static final int RESULT_CODE_UNAUTHORIZED = 401 ; //Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
    public static final int RESULT_CODE_FORBIDDEN =403;// Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
    public static final int RESULT_CODE_NOT_FOUND = 404;// NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
    public static final int RESULT_CODE_NOT_ACCEPTABLE = 406;// Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
    public static final int RESULT_CODE_GONE = 410;// Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
    public static final int RESULT_CODE_UNPROCESABLE_ENTITY = 422 ;//Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
    public static final int RESULT_CODE_INTERNAL_SERVER_ERROR = 500;// INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
}
