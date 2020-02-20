/**
 * Jquery Ajax call utils.
 * 
 * Ex>
 * 
 * Ajax.setContext("컨텍스트명"); 사용 시 필수 호출
 * 
 * Ajax.post().url(url).requestBodyByForm(formId).success(successCallback).failure(failureCallback).execute();
 * 
 * Ajax.post().sync().url(url).requestBody(JSON.stringify(data)).success(successCallback).execute();
 * 
 * Ajax.get().sync().url(url).success(successCallback).execute();
 * 
 * Ajax.get().url(url).queryString("userId=" + userId).success(successHandler).execute();
 * 
 */
var AjaxUtils = function(httpMethod, context, targetServerUrl) {
    this._context = context;
    this._targetServerUrl = targetServerUrl != null ? targetServerUrl : null; // Default API Server URL
    this._httpMethod = httpMethod;
    this._sync = false; // Default false
    this._url = null;
    this._data = "";
    this._responseType = "json"; // Default json
    this._contentType = null;
    this._contentLength = null;
    this._processData = null;
    this._enctype = null;
    this._successCallback = null;
    this._failureCallback = null;

    this.callAjax = function() {
        $.ajax(this.build());
    };

    this.build = function() {
        var request = new Object();
        request["context"] = this;
        request["method"] = this._httpMethod;
        request["url"] = this._targetServerUrl != null ? targetServerUrl : "" + this._context  + this._url;
        request["data"] = this._data;
        request["dataType"] = this._responseType;
        request["cache"] = false;
        request["crossDomain"] = true;
        request["xhrFields"] = {
                withCredentials: true
        };

        if (this._contentType != null) {
            request["contentType"] = this._contentType;
        }

        if (this._sync) {
            request["async"] = false;
        }

        if (this._processData != null) {
            request["processData"] = this._processData;
        }

        if (this._enctype != null) {
            request["enctype"] = this._enctype;
        }

        request["beforeSend"] = function(jqXHR, setting) {
            jqXHR.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            if (this._contentLength != null) {
                jqXHR.setRequestHeader("Content-Length", this._contentLength);
            }
            
            if (this._sync == false) {
                showOverlayDiv();
            }
        };

        request["success"] = function(response, textStatus, jqXHR) {
            if (jqXHR.status == 200) {
                if (this._successCallback != null) {
                    this._successCallback(response, jqXHR);
                }
            } else {
                if (this._failureCallback != null) {
                    this._failureCallback(response, jqXHR);
                } else {
                    this.showErrorMessage(jqXHR.status);
                }
            }
        };

        request["error"] = function(jqXHR, textStatus, errorThrown) {
            if (jqXHR.status == 401 || jqXHR.status == 403 || jqXHR.status == 0 ||(jqXHR.status != 500 && jqXHR.state() == "rejected")) {
                location.replace(this._context + "/login?error=true&errorCode=730");
            } else {
                if (this._failureCallback != null) {
                    this._failureCallback(jqXHR);
                } else {
                    this.showErrorMessage(jqXHR, errorThrown);
                }
            }
        };

        request["complete"] = function(jqXHR) {
            if (this._sync == false) {
                hideOverlayDiv();
            }
        };
        return request;
    };

    this.showErrorMessage = function(jqXHR, errorThrown) {
        var statusCode = jqXHR.status;
        var errorMessage = "";
        if (statusCode == 400) { // 400:"Bad Request"
            errorMessage = "잘못된 요청입니다.";
        } else if (statusCode == 404) { // 404:"Not Found"
            errorMessage = "요청을 찾을 수 없습니다";
        } else if (statusCode == 500) { // 500:"Internal Server Error"
            errorMessage = "서버 내부 오류 입니다.";
        } else {
            errorMessage = "에러코드 : " + statusCode;
        }
        if (errorMessage != "") {
            alert(errorMessage);
        }
    };
};

/**
 * Set sync request
 */
AjaxUtils.prototype.sync = function() {
    this._sync = true;
    return this;
};

/**
 * Set targetServerUrl. Default http://API-Server-HostAddress:Port
 */
AjaxUtils.prototype.targetServerUrl = function(targetServerUrl) {
    this._targetServerUrl = targetServerUrl;
    return this;
};

/**
 * API URL
 */
AjaxUtils.prototype.url = function(url) {
    this._url = url;
    return this;
};

/**
 * Set queryString request data from formId. contentType =
 * application/x-www-form-urlencoded; charset=UTF-8
 */
AjaxUtils.prototype.queryStringByForm = function(formId) {
    this._data = formId != null ? $("#" + formId).serialize() : "";
    this._contentType = "application/x-www-form-urlencoded; charset=UTF-8";
    return this;
};

/**
 * Set queryString contentType = application/x-www-form-urlencoded;
 * charset=UTF-8
 */
AjaxUtils.prototype.queryString = function(queryString) {
    this._data = queryString;
    this._contentType = "application/x-www-form-urlencoded;charset=UTF-8";
    return this;
};

/**
 * Set json requestBody data from formId contentType = appliaction/json;
 * charset=UTF-8
 */
AjaxUtils.prototype.requestBodyByForm = function(formId) {
    this._data = formId != null ? JSON.stringify($("#" + formId)
            .serializeObject()) : "";
    this._contentType = "application/json;charset=UTF-8";
    return this;
};

/**
 * Set json requestBody contentType = appliaction/json; charset=UTF-8
 */
AjaxUtils.prototype.requestBody = function(jsonString) {
    this._data = jsonString;
    this._contentType = "application/json;charset=UTF-8";
    return this;
};

/**
 * Set formData
 */
AjaxUtils.prototype.formData = function(formData) {
    this._data = formData;
    this._contentType = false;
    this._processData = false;
    return this;
};

/**
 * Set successCallbackFunction
 */
AjaxUtils.prototype.success = function(successCallback) {
    this._successCallback = successCallback;
    return this;
};

/**
 * Set failureCallbackFunction
 */
AjaxUtils.prototype.failure = function(failureCallback) {
    this._failureCallback = failureCallback;
    return this;
};

/**
 * Set enctype
 */
AjaxUtils.prototype.enctype = function(enctype) {
    this._enctype = enctype;
    return this;
};

/**
 * Set processType
 */
AjaxUtils.prototype.processData = function(processData) {
    this._processData = processData;
    return this;
};

/**
 * Set responseType. json/text...
 */
AjaxUtils.prototype.responseType = function(responseType) {
    this._responseType = responseType;
    return this;
};

/**
 * Set contentLength.
 */
AjaxUtils.prototype.contentLength = function(contentLength) {
    this._contentLength = contentLength;
    return this;
};

/**
 * Execute
 */
AjaxUtils.prototype.execute = function() {
    this.callAjax();
};

var Ajax = function() {
    this._context = null;
};

Ajax.setContext = function(context) {
    this._context = context;
};

Ajax.get = function() {
    return new AjaxUtils("GET", this._context);
};

Ajax.post = function() {
    return new AjaxUtils("POST", this._context);
};

Ajax.put = function() {
    return new AjaxUtils("PUT", this._context);
};