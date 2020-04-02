package me.artish1.CrystalClash.Util;

import org.json.simple.JSONObject;

public class JsonMessageText {
	
	 private JSONObject jsonObject;

	    @SuppressWarnings("unchecked")
		public JsonMessageText(String message) {

	        jsonObject = new JSONObject();

	        jsonObject.put("text",message);

	    }

	    public JSONObject getJsonObject() {
	        return jsonObject;
	    }
	
}
