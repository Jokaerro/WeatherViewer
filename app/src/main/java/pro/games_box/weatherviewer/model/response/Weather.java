package pro.games_box.weatherviewer.model.response;

/**
 * Created by TESLA on 06.04.2017.
 */

public class Weather {
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Rain rain = new Rain();
    public Snow snow = new Snow()	;
    public Clouds clouds = new Clouds();
    private String CityName;
    private String dateTime;

    public byte[] iconData;

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public String getDateTime(){
        return this.dateTime;
    }

    public void setCityName(String city)
    {
        CityName = city;
    }

    public String getCityName(){
        return CityName;
    }

    public  class CurrentCondition {
        private int weatherId;
        private String condition;
        private String descr;
        private String icon;


        private float pressure;
        private float humidity;

        public int getWeatherId() {return weatherId;}
        public void setWeatherId(int weatherId) {
            this.weatherId = weatherId;
        }
        public String getCondition() {
            return condition;
        }
        public void setCondition(String condition) {
            this.condition = condition;
        }
        public String getDescr() {
            return descr;
        }
        public void setDescr(String descr) {
            this.descr = descr;
        }
        public String getIcon() {
            return icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
        public float getPressure() {
            return pressure;
        }
        public void setPressure(float pressure) {
            this.pressure = pressure;
        }
        public float getHumidity() {
            return humidity;
        }
        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }

    }

    public  class Temperature {
        private float temp;
        private float minTemp;
        private float maxTemp;

        public float getTemp() {
            return temp;
        }
        public void setTemp(float temp) {
            this.temp = temp;
        }
        public float getMinTemp() {
            return minTemp;
        }
        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }
        public float getMaxTemp() {
            return maxTemp;
        }
        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }

    }

    public  class Wind {
        private float speed;
        private float deg;
        public float getSpeed() {
            return speed;
        }
        public void setSpeed(float speed) {
            this.speed = speed;
        }
        public float getDeg() {
            return deg;
        }
        public void setDeg(float deg) {
            this.deg = deg;
        }


    }

    public  class Rain {
        private String time;
        private float ammount;
        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public float getAmmount() {
            return ammount;
        }
        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }

    }

    public  class Snow {
        private String time;
        private float ammount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmmount() {
            return ammount;
        }

        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }
    }

    public  class Clouds {
        private int perc;

        public int getPerc() {
            return perc;
        }

        public void setPerc(int perc) {
            this.perc = perc;
        }

    }
}
