package com.yibuwulianwang.skill.baidu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import com.baidu.dueros.bot.BaseBot;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.SessionEndedRequest;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.OutputSpeech.SpeechType;
import com.baidu.dueros.data.response.Reprompt;
import com.baidu.dueros.data.response.card.TextCard;
import com.baidu.dueros.model.Response;
import com.yibuwulianwang.handle.Handle;

public class SkillBot extends BaseBot {

	ArrayList<String> changjingList = new ArrayList<String>();

	ArrayList<String> caozuoList = new ArrayList<String>();

	static String changjingmoshiStr = null;

	/**
	 * 重写BaseBot构造方法
	 * 
	 * @param request
	 *            servlet Request作为参数
	 * @throws IOException
	 *             抛出异常
	 */
	public SkillBot(HttpServletRequest request) throws IOException {
		super(request);
		String privateKey = 
				  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANd0XEVa8CJ4I35B\n"
				+ "N19tePLRJ6GvMBl86BL3O2w6H+1khxsqbdiz82ZqICUPK2ESNCmlCGpzO35oKJN/\n"
				+ "q+u1BpaUePvvoFScBD6l3TFw0iP2NjlwzmnPut2V8SRGKCn5F9rWJrIIyqYRja+C\n"
				+ "3wwqWukfRvBS9RILE0KkZpYKIoxRAgMBAAECgYBBGyx22NAP2nX/RP3SnGYcGg/7\n"
				+ "z9CdIx2N/Z+7SKU1O1eIYf3nqhk3LYKhMEBPiQyyOOieIkwsGpWIXOA6ze07LZj1\n"
				+ "hs/r4YfjgoYgLX+lmNYE0CkrTxgAF8xvaIWu33Gx/jAyaDuMY0/ePgA623EjeCJW\n"
				+ "bZDWq7qWhNh/OGmYQQJBAOzahbgqbVdnILb1eRm/qDmQd1ekdRPYBuGDS0DKFAFx\n"
				+ "lgq6JTmg0SLLeuOzFbeXBSgXg5nADytLlUh0iVlOZ/0CQQDo3wDLKv9K+gU86NKc\n"
				+ "q/Xp5VYOBXU4xEpgFq4yByy0KjSp48cuOg9Fxxj8eG0EHORPodGNhj1Ln7sEXfXt\n"
				+ "ktPlAkBOS24okt3SJqUJ1dtSR6i0Xq0Uq6iIBsE/isc1g5dZYtLWePIVOrPnbJwS\n"
				+ "6QAk0CdEOYGnMXXSOgn/CYbdnq2BAkEAl6WRaLzpXzQN8SpU7P32uIhGT/8x5PO+\n"
				+ "03AxpBaUAAd0ICZa8TmR9IjBNrhdlc4Sx+DDC/ydMgAk06U0n698GQJAYucyAEK5\n"
				+ "uztdFkMCLzEynjhISga2A3nwtreeb3Vic/HtSWq55/F1FpyUH0IT/pqbN5RICWUf\n" 
				+ "/YdNBjzbvViroQ==";
		// privateKey为私钥内容,0代表你的Bot在DBP平台debug环境，1或者其他整数代表online环境,botMonitor对象已经在bot-sdk里初始化，可以直接调用
		this.botMonitor.setEnvironmentInfo(privateKey, 1);
	}

	/**
	 * 重写BaseBot构造方法
	 * 
	 * @param request
	 *            Request字符串
	 * @throws IOException
	 *             抛出异常
	 */
	public SkillBot(String request) throws IOException {
		super(request);
	}

	private Response reTextCard(String ret) {
		// 新建文本卡片
		TextCard textCard = new TextCard(ret);
		// 设置链接地址
		// textCard.setUrl("www:....");
		// 设置链接内容
		// textCard.setAnchorText("setAnchorText");
		// 添加引导话术
		// textCard.addCueWord("欢迎进入");

		setSessionAttribute("key_1", "value_1");
		setSessionAttribute("key_2", "value_2");

		// 新建返回的语音内容
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, ret);

		OutputSpeech myoutputSpeech = new OutputSpeech(SpeechType.PlainText, "请您说出想要设置的场景模式");
		Reprompt reprompt = new Reprompt(myoutputSpeech);

		// 构造返回的Response
		Response response = new Response(outputSpeech, textCard, reprompt);

		return response;
	}

	/**
	 * 重写onLaunch方法，处理onLaunch对话事件
	 * 
	 * @param launchRequest
	 *            LaunchRequest请求体
	 * @see com.baidu.dueros.bot.BaseBot#onLaunch(com.baidu.dueros.data.request.LaunchRequest)
	 */
	@Override
	protected Response onLaunch(LaunchRequest launchRequest) {
		changjingList.clear();
		caozuoList.clear();
		return reTextCard("欢迎使用，您可以使用该技能添加控制智能家居的场景模式。如：“休闲模式”。");
	}

	/**
	 * 重写onSessionEnded事件，处理onSessionEnded对话事件
	 * 
	 * @param sessionEndedRequest
	 *            SessionEndedRequest请求体
	 * @see com.baidu.dueros.bot.BaseBot#onSessionEnded(com.baidu.dueros.data.request.SessionEndedRequest)
	 */
	@Override
	protected Response onSessionEnded(SessionEndedRequest sessionEndedRequest) {
		String str = "已为您添加" + arrayListToString(changjingList) + ",您需要叫小度“发现设备”后，便可使用！";
		return reTextCard(str);
	}

	/**
	 * 重写onInent方法，处理onInent对话事件
	 * 
	 * @param intentRequest
	 *            IntentRequest请求体
	 * @see com.baidu.dueros.bot.BaseBot#onInent(com.baidu.dueros.data.request.IntentRequest)
	 */
	@Override
	protected Response onInent(IntentRequest intentRequest) {
		if ("set_qingjingmoshi".equals(intentRequest.getIntentName())) {// 确认意图
			if (getSlot("changjingmoshi") != null) {
				changjingmoshiStr = getSlot("changjingmoshi");
				changjingList.add(changjingmoshiStr);
				return reTextCard("您想要为" + changjingmoshiStr + "添加什么操作呢？");
			}
			return reTextCard("您需要设置什么情景模式呢？如：“早安模式”。");
		} else if ("ok_qingjingmoshi".equals(intentRequest.getIntentName())) {
			String str = changjingmoshiStr;
			changjingmoshiStr = null;
			Handle.printSkillLog("onInent()", arrayListToString(caozuoList));
			return reTextCard("好的，已为您添加：" + str);
		} else if ("ai.dueros.common.default_intent".equals(intentRequest.getIntentName())) {// 缺省意图
			return computeAnswer();
		} else {
			return reTextCard(getQuery());
		}
	}

	private Response computeAnswer() {
		String query = getQuery();
		com.yibuwulianwang.handle.Handle.printSkillLog("getQuery()", query);
		if (query.contains("打开") || query.contains("关闭") || query.contains("调整") || query.contains("设置")
				|| query.contains("开") || query.contains("关") || query.contains("播放") || query.contains("音乐")
				|| query.contains("电视") || query.contains("空调")) {
			caozuoList.add(query);
			return reTextCard("好的，还有呢？");
		} else {
			return reTextCard("抱歉，没有识别到您的控制设备指令，请语音输入控制指令，如“打开空调”，“关闭窗帘”等。");
		}
	}
	
    // ArrayList类型转成String类型
    public String arrayListToString(ArrayList<String> arrayList) {
        String result = "";
        if (arrayList != null && arrayList.size() > 0) {
            for (String item : arrayList) {
                // 把列表中的每条数据用逗号分割开来，然后拼接成字符串
                result += item + ",";
            }
            // 去掉最后一个逗号
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
