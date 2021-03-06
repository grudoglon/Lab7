    package models;

    import javax.xml.bind.annotation.XmlAccessType;
    import javax.xml.bind.annotation.XmlAccessorType;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;
    import java.util.ArrayList;

    @XmlRootElement(name = "pens")
    @XmlAccessorType(XmlAccessType.FIELD)
    public class Pens
    {
        @XmlElement(name = "pen")
        private ArrayList<Pen> pens = new ArrayList<Pen>();

        public ArrayList<Pen> getPens() {
            return pens;
        }

        public void setPens(ArrayList<Pen> pens1) {
            this.pens = pens1;
        }
    }