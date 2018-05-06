package com.yibuwulianwang.oauth.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yibuwulianwang.config.Config;
import com.yibuwulianwang.handle.Handle;

@Controller
@RequestMapping("/oauthserver")
public class OAuthServerController {

	static String accessToken = null, oauthorizationCode = null;

	@RequestMapping("/getOAuthorizationCode")
	public Object getOAuthorizationCode(HttpServletRequest request, HttpServletResponse response,Model model)
			throws URISyntaxException, OAuthSystemException {
		Handle.printRequestLog(request.getRequestURI(), "getOAuthorizationCode(请求AuthorizationCode)");
		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			
			Handle.printRequestLog("ClientId:", oauthRequest.getClientId());
			Handle.printRequestLog("ClientSecret:", oauthRequest.getClientSecret());
			Handle.printRequestLog("RedirectURI:", oauthRequest.getRedirectURI());
			Handle.printRequestLog("ResponseType:", oauthRequest.getResponseType());
			Handle.printRequestLog("State:", oauthRequest.getState());
			Handle.printRequestLog("Scopes:", String.valueOf(oauthRequest.getScopes()));

			// 验证传入的客户端id是否正确
			if (!oauthRequest.getClientId().equals(Config.OAUTH_CLIENT_ID)) {
				OAuthResponse response1 = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("GET_CODE_INVALID_CLIENT_ID_ERR").buildJSONMessage();
				return new ResponseEntity(response1.getBody(), HttpStatus.valueOf(response1.getResponseStatus()));
			}
			// 如果用户没有登录，跳转到登陆页面
			if (!login(request,model)) {// 登录失败时跳转到登陆页面
				return "oauthlogin";// 指向jsp
			}
			
			String authorizationCode = null;
			// responseType目前仅支持CODE，另外还有TOKEN
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());// 生成授权码 MD5
				authorizationCode = oauthIssuerImpl.authorizationCode();
				this.oauthorizationCode = authorizationCode;
			}

			// 进行OAuth响应构建
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
					HttpServletResponse.SC_FOUND);
			// 设置授权码
			builder.setCode(authorizationCode);
			
			// 得到到客户端重定向URL地址
			String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

			// 构建响应
			final OAuthResponse response2 = builder.location(redirectURI).buildQueryMessage();

			// 根据OAuthResponse返回ResponseEntity响应
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response2.getLocationUri()));
			
			Handle.printResponseLog(request.getRequestURI(),"HttpHeaders:"+headers.getLocation());

			return new ResponseEntity(headers, HttpStatus.valueOf(response2.getResponseStatus()));// 响应http请求
		} catch (OAuthProblemException e) {

			// 出错处理
			String redirectUri = e.getRedirectUri();
			if (OAuthUtils.isEmpty(redirectUri)) {
				// 告诉客户端没有传入redirectUri直接报错
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.add("Content-Type", "application/json; charset=utf-8");
				JSONObject errJson = new JSONObject();
				errJson.put("INVALID_REDIRECT_URI_ERR", HttpStatus.NOT_FOUND.value());

				return new ResponseEntity(errJson.toString(), responseHeaders, HttpStatus.NOT_FOUND);
			}
			// 构建响应，返回错误消息
			final OAuthResponse response1 = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response1.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(response1.getResponseStatus()));
		}
	}

	@RequestMapping("/getAccessToken")
	public HttpEntity getAccessToken(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
		Handle.printRequestLog(request.getRequestURI(), "getAccessToken(请求AccessToken)");

		try {
			// 构建OAuth请求
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
			// 验证提交的客户端id是否正确
			if (!oauthRequest.getClientId().equals(Config.OAUTH_CLIENT_ID)) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT).setErrorDescription("INVALID_CLIENT_ID_ERR")
						.buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			// 验证客户端安全KEY是否正确
			if (!oauthRequest.getClientSecret().equals(Config.OAUTH_CLIENT_SECRET)) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
						.setErrorDescription("INVALID_CLIENT_SECRET_ERR").buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
			// 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				if (!authCode.equals(oauthorizationCode)) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("INVALID_AUTH_CODE ERR").buildJSONMessage();
					return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
				}
			}
			
			

			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			final String refreshToken = oauthIssuerImpl.refreshToken();
			final String authorizationCode = oauthIssuerImpl.authorizationCode();
			if(null != oauthRequest.getRefreshToken()) {
				Handle.printRequestLog(request.getRequestURI(),"请求刷新accessToken");
				Handle.printRequestLog("Old RefreshToken:", oauthRequest.getRefreshToken());
			}else {
				Handle.printRequestLog(request.getRequestURI(),"首次请求accessToken");
			}
			Handle.printRequestLog("ClientId:", oauthRequest.getClientId());
			Handle.printRequestLog("ClientSecret:", oauthRequest.getClientSecret());
			Handle.printRequestLog("RedirectURI:", oauthRequest.getRedirectURI());
			Handle.printRequestLog("GrantType:", oauthRequest.getGrantType());
			Handle.printRequestLog("Username:", oauthRequest.getUsername());
			Handle.printRequestLog("Password:", oauthRequest.getPassword());
			Handle.printRequestLog("Scopes:", String.valueOf(oauthRequest.getScopes()));
			Handle.printResponseLog("new AccessToken:", accessToken);
			Handle.printResponseLog("New RefreshToken:", refreshToken);
			Handle.printResponseLog("New AuthorizationCode:", authorizationCode);
	
			this.accessToken = accessToken;
			// 生成OAuth响应
			OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken)
					.setRefreshToken(refreshToken)
					.setExpiresIn(Config.ACCESS_TOKEN_TIME)// token有效时长
					.setScope("basic email")// 权限
					.buildJSONMessage();
			
			Handle.printResponseLog(request.getRequestURI(),"OAuthResponse Headers:"+response.getHeaders().toString());
			Handle.printResponseLog(request.getRequestURI(),"OAuthResponse Body:"+response.getBody());


			// 根据OAuthResponse生成ResponseEntity
			return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));

		} catch (OAuthProblemException e) {
			// 构建错误响应
			OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
		}
	}

	/**
	 * 验证accessToken
	 *
	 * @param accessToken
	 * @return
	 */
	@RequestMapping(value = "/checkAccessToken", method = RequestMethod.POST)
	public ResponseEntity checkToken(@RequestParam("accessToken") String accessToken) {
		boolean b = this.accessToken.equals(accessToken);// 在缓存中认证token
		Handle.printResponseLog(this.getClass().getName(), "认证Token:" + accessToken + "的结果为" + b);
		return b ? new ResponseEntity(HttpStatus.valueOf(HttpServletResponse.SC_OK))
				: new ResponseEntity(HttpStatus.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
	}

	/**
	 * 根据accessToken获取用户数据
	 * 
	 * @param request
	 * @return
	 * @throws OAuthSystemException
	 */
	@RequestMapping("/getUserData")
	private HttpEntity getUserData(HttpServletRequest request) throws OAuthSystemException {
		Handle.printRequestLog(request.getRequestURI(), "getUserData(请求获取用户数据)");
		try {
			// 构建OAuth资源请求
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
			// 获取Access Token
			String accessToken = oauthRequest.getAccessToken();
			// 验证Access Token
			try {
				if (!this.accessToken.equals(accessToken)) {//认证accessToken的合法性
					OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
							.setRealm("RESOURCE_SERVER_NAME").setError(OAuthError.ResourceResponse.INVALID_TOKEN)
							.buildHeaderMessage();
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.add("Content-Type", "application/json; charset=utf-8");
					JSONObject errJson = new JSONObject();
					errJson.put("INVALID_REDIRECT_URI ERR", HttpStatus.NOT_FOUND.value());
					return new ResponseEntity(errJson.toString(), responseHeaders, HttpStatus.UNAUTHORIZED);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 获取用户数据
			JSONObject errJson = new JSONObject();
			errJson.put("UserName", Config.OAUTH_USERNAME);
			errJson.put("PassWord", Config.OAUTH_PASSWORD);

			return new ResponseEntity(errJson.toString(), HttpStatus.OK);// 拿到数据并响应请求
		} catch (OAuthProblemException e) {
			// 存在错误信息
			String errorCode = e.getError();
			if (OAuthUtils.isEmpty(errorCode)) {
				OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setRealm("RESOURCE_SERVER_NAME_EMPTY_ERR").buildHeaderMessage();
				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
						oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
			}
			OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
					.setRealm("RESOURCE_SERVER_NAME_ERR").setError(e.getError()).setErrorDescription(e.getDescription())
					.setErrorUri(e.getUri()).buildHeaderMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	private boolean login(HttpServletRequest request,Model model) {
		if ("get".equalsIgnoreCase(request.getMethod())) {
			request.setAttribute("error", "登录失败:请使用POST请求");
			model.addAttribute("error", "登录失败:请使用POST请求");
			return false;
		}
		// 从输入框中拿到输入的账号信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username.equals("") || password.equals("")) {
			model.addAttribute("error", "登录失败:用户名或密码不能为空");
			request.setAttribute("error", "登录失败:用户名或密码不能为空");
			Handle.printRequestLog("Login", "登录失败：用户名或�?�密码不能为�??");
			return false;
		}
		try {
			if (!Config.OAUTH_USERNAME.equals(username)) {
				model.addAttribute("error", "登录失败:用户名不正确");
				request.setAttribute("error", "登录失败:用户名或密码不能为空");
				Handle.printRequestLog("Login", "登录失败：用户名不正�??");
				return false;
			} else if (!Config.OAUTH_PASSWORD.equals(password)) {
				model.addAttribute("error", "登录失败：密码不正确");
				request.setAttribute("error", "登录失败:用户名或密码不能为空");
				Handle.printRequestLog("Login", "登录失败：密码不正确");
				return false;
			} else {
				Handle.printRequestLog("Login", "登录成功");
				return true;
			}
		} catch (Exception e) {
			model.addAttribute("error", "登录失败:用户名或密码不能为空");
			request.setAttribute("error", "登录失败:" + e.getClass().getName());
			Handle.printRequestLog("Login", "登录失败:" + e.getClass().getName());
			return false;
		}
	}
}
