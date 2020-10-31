package managers;

import models.Pens;
import models.Pen;
import exceptions.DuplicateIdException;
import javax.xml.bind.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionManager {


    private LocalDate initDate;
    private Pens pensArray;
    private File xmlCollection;
    private Long maxId = 0L;
    private List<Pen> collection;


    public CollectionManager(String fileName) throws IOException {
        if (fileName != null) {
            xmlCollection = new File(fileName);
            try {
                if (!xmlCollection.exists()) throw new FileNotFoundException();
            } catch (FileNotFoundException ex) {
                System.out.println("Файл не существует.");
                System.exit(1);
            }

            this.load();
            initDate = LocalDate.now();
            System.out.println("Коллекция загружена");
        }else {
            System.out.println("Файл не найден");
            System.exit(1);
        }
    }

    public CollectionManager(){
        this.collection = new ArrayList<>();
        this.initDate = LocalDate.now();
    }

    public CollectionManager(ArrayList<Pen> collection){
        this.initDate = LocalDate.now();

        this.collection = Collections.synchronizedList(collection);
        this.maxId = (long) (collection.size() + 1);
    }


    public void addElement(Pen pen){
        maxId+=1;

        this.getPenCollection().add(pen);
    }


    public boolean addIfMin(Pen pen){
        List<Pen> cities = this.getPenCollection().stream().sorted().collect(Collectors.toList());
        if (cities.isEmpty()) {
            return false;
        }

        Pen firstPen = cities.get(0);
        if (firstPen.compareTo(pen) > 0) {
            addElement(pen);
            return true;
        } else {
            return false;
        }
    }




    public void removeLower(Pen pen){
        List<Pen> cities = this.getPenCollection().stream().sorted().collect(Collectors.toList());
        cities.forEach(x -> {
            if(x.compareTo(pen) < 0){
                this.getPenCollection().remove(x);
            }
        });
    }



    public boolean remove(long id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, Pen> entry = findById(id).entrySet().iterator().next();
            this.getPenCollection().remove(entry.getValue());
            return true;
        }

        return false;
    }


    public boolean update(Pen pen, Long id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, Pen> entry = findById(id).entrySet().iterator().next();
            Pen updPen = entry.getValue();
            updPen.setName(pen.getName());
            updPen.setWidth_of_kernel(pen.getWidth_of_kernel());
            updPen.setExists(pen.getExists());
            updPen.setWeight(pen.getWeight());
            updPen.setAmount(pen.getAmount());
            updPen.setLength_of_kernel(pen.getLength_of_kernel());

            this.getPenCollection().set(entry.getKey(), updPen);
            return true;
        }

        return false;
    }

    public boolean checkIdExist(Long id){
        for(int i=0;i<this.getPenCollection().size();i++) {
            if(this.getPenCollection().get(i).getId().equals(id)) {
                return true;
            }
        }

        return false;
    }


    private Map<Integer, Pen> findById(Long id){
        Map<Integer, Pen> map = new HashMap<>();
        for(int i=0;i<this.getPenCollection().size();i++) {
            if(this.getPenCollection().get(i).getId().equals(id)) {
                map.put(i, this.getPenCollection().get(i));
                return map;
            }
        }

        return null;
    }


    public ArrayList<Pen> findByName(String name){
        ArrayList<Pen> cities = new ArrayList<Pen>();
        this.getPenCollection().forEach(x ->{
            if(x.getName().contains(name))
                cities.add(x);
        });
        return cities;
    }


    private void load() throws IOException {
        try{
            if(!xmlCollection.canWrite() || !xmlCollection.canRead()) throw new SecurityException();
        }catch (SecurityException ex){
            System.out.println("Файл недоступен");
            System.exit(1);
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(xmlCollection))) {
            System.out.println("Загружаем коллекцию из файла...");

            JAXBContext jaxbContext = JAXBContext.newInstance(Pens.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            pensArray = (Pens) jaxbUnmarshaller.unmarshal(inputStreamReader);
            collection = pensArray.getCities();
            pensArray.getCities().forEach(x-> {
                if(maxId < x.getId())
                    maxId = x.getId();
            });

            checkDuplicateId();

        } catch (JAXBException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }


    public void clear(){
        this.getPenCollection().clear();
    }



    public void save(){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(xmlCollection));
            JAXBContext jaxbContext = JAXBContext.newInstance(Pens.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(pensArray, outputStreamWriter);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void shuffle(){
        Collections.shuffle(this.getPenCollection());
    }

    public ArrayList<Pen> sortById(){
        ArrayList<Pen> sortColl = new ArrayList<Pen>(this.getPenCollection());
        sortColl.sort(Comparator.comparing(Pen::getId));

        return sortColl;
    }


    public ArrayList<Pen> sortByWeight(){
        ArrayList<Pen> sortColl = new ArrayList<Pen>(this.getPenCollection());
        sortColl.sort(Comparator.comparing(Pen::getWeight));

        return sortColl;
    }


    private void checkDuplicateId(){
        List<Pen> cities = sortById();

        for(int i=1;i<cities.size();i++) {
            if(cities.get(i-1).getId().equals(cities.get(i).getId())) {
                throw new DuplicateIdException("Поле id должно быть уникальным");
            }
        }
    }

    public List<Pen> getPenCollection() {
        return collection;
    }

    public void setPenCollection(ArrayList<Pen> collection){
        this.collection = collection;
    }

    public LocalDate getInitDate() {
        return initDate;
    }
}
