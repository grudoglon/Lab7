    package models;

    import javax.xml.bind.annotation.XmlAccessType;
    import javax.xml.bind.annotation.XmlAccessorType;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;
    import java.util.ArrayList;

    @XmlRootElement(name = "cities")
    @XmlAccessorType(XmlAccessType.FIELD)
    public class Pens
    {
        @XmlElement(name = "pen")
        private ArrayList<Pen> cities = new ArrayList<Pen>();

        public ArrayList<Pen> getCities() {
            return cities;
        }

        public void setCities(ArrayList<Pen> cities) {
            this.cities = cities;
        }
    }