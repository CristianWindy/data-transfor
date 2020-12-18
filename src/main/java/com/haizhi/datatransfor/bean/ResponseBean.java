package com.haizhi.datatransfor.bean;

import java.util.List;

/**
 * @Author windycristian
 * @Date: 2020/9/21 18:26
 **/
public class ResponseBean {
    /**
     * id : 121212
     * result : {"code":1,"data":[{"fieldValues":[{"codeValue":"","field":"thread_name","isCode":0,"value":"sjjhgaw2ycyj"}],"sourceId":"DS-00000006"}],"msg":"","sign":""}
     * jsonrpc : 2.0
     */

    private String id;
    private ResultBean result;
    private String jsonrpc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public static class ResultBean {
        /**
         * code : 1
         * data : [{"fieldValues":[{"codeValue":"","field":"thread_name","isCode":0,"value":"sjjhgaw2ycyj"}],"sourceId":"DS-00000006"}]
         * msg :
         * sign :
         */

        private int code;
        private String msg;
        private String sign;
        private List<DataBean> data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * fieldValues : [{"codeValue":"","field":"thread_name","isCode":0,"value":"sjjhgaw2ycyj"}]
             * sourceId : DS-00000006
             */

            private String sourceId;
            private List<FieldValuesBean> fieldValues;

            public String getSourceId() {
                return sourceId;
            }

            public void setSourceId(String sourceId) {
                this.sourceId = sourceId;
            }

            public List<FieldValuesBean> getFieldValues() {
                return fieldValues;
            }

            public void setFieldValues(List<FieldValuesBean> fieldValues) {
                this.fieldValues = fieldValues;
            }

            public static class FieldValuesBean {
                /**
                 * codeValue :
                 * field : thread_name
                 * isCode : 0
                 * value : sjjhgaw2ycyj
                 */

                private String codeValue;
                private String field;
                private int isCode;
                private String value;

                public String getCodeValue() {
                    return codeValue;
                }

                public void setCodeValue(String codeValue) {
                    this.codeValue = codeValue;
                }

                public String getField() {
                    return field;
                }

                public void setField(String field) {
                    this.field = field;
                }

                public int getIsCode() {
                    return isCode;
                }

                public void setIsCode(int isCode) {
                    this.isCode = isCode;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
