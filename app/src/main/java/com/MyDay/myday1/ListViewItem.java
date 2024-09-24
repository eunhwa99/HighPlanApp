package com.MyDay.myday1;

public class ListViewItem {
    /**
     * 일정 데이터에 저장할 때 저장 방식: 일정 이름과 걸린 시간 저장
     */

        public ListViewItem(String namestr, String timestr) {
            this.namestr = namestr;
            this.timestr = timestr;
        }

        private String namestr; //일정 이름
        private String timestr; //걸린 시간


        public void setName(String Name) {
            namestr = Name;
        }

        public void setTime(String Time) {
            timestr = Time;
        }

        public String getName() {
            return this.namestr;
        }

        public String getTime() {
            return this.timestr;
        }

}
