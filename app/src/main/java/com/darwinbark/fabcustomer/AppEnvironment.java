package com.darwinbark.fabcustomer;


public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            //return "QylhKRVd";
            return "p9qFFZqb";

        }

        @Override
        public String merchant_ID() {
            //return "5960507";
            return " 7447714";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            //return "seVTUgzrgE";
            return "EQIBm758lb";
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            // return "QylhKRVd";
            return "HGOQTI3r";


        }
        @Override
        public String merchant_ID() {
            //return "5960507";
            return " 7447714";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            //return "seVTUgzrgE";
            return "WT6LTW5Lgz";

        }

        @Override
        public boolean debug() {
            return false;
        }
    };



    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
