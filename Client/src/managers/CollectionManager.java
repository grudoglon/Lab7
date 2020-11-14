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
    private Integer maxId = 0;
    private List<Pen> collection;

    /**
     * Класс, который работает с коллекцией
     * @param fileName
     * @throws IOException
     */
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
        this.maxId = (collection.size() + 1);
    }

    /**
     * добавляет новый элемент
     * @param pen
     */
    public void addElement(Pen pen){
        maxId+=1;

        this.getPenCollection().add(pen);
    }

    /**
     * добавляет новый элемент с условием
     * @param pen
     * @return
     */
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




    public ArrayList<Integer> removeLower(Pen pen){
        List<Pen> pens = this.getPenCollection().stream().sorted().collect(Collectors.toList());
        ArrayList<Integer> arr = new ArrayList<>();
        pens.forEach(x -> {
            if(x.compareTo(pen) < 0){
                arr.add(x.getId());
                this.getPenCollection().remove(x);
            }
        });
        return arr;
    }

    /**
     * удаляет элемент коллекции
     * @param id
     */
    public boolean remove(long id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, Pen> entry = findById(id).entrySet().iterator().next();
            this.getPenCollection().remove(entry.getValue());
            return true;
        }

        return false;
    }

    /**
     * обновляет коллекцию по его id
     * @param pen
     * @param id
     */
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

    /**
     * ищет элемент по id
     * @param id
     * @return
     */
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

    /**
     * находит элементы по его имени
     * @param name
     * @return
     */
    public ArrayList<Pen> findByName(String name){
        ArrayList<Pen> cities = new ArrayList<Pen>();
        this.getPenCollection().forEach(x ->{
            if(x.getName().contains(name))
                cities.add(x);
        });

        return cities;
    }

    /**
     * загрузка xml из файла
     * @throws IOException
     */
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

    /**
     * очищает коллекцию
     */
    public void clear(){
        this.getPenCollection().clear();
    }


    /**
     * сохранить коллекцию в xml
     */
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

    /**
     * перемешивает элементы в случайном порядке
     */
    public void shuffle(){
        Collections.shuffle(this.getPenCollection());
    }

    public ArrayList<Pen> sortById(){
        ArrayList<Pen> sortColl = new ArrayList<Pen>(this.getPenCollection());
        sortColl.sort(Comparator.comparing(Pen::getId));

        return sortColl;
    }

    /**
     * сортирует по часовому поясу
     * @return
     */
    public ArrayList<Pen> sortByWeight(){
        ArrayList<Pen> sortColl = new ArrayList<Pen>(this.getPenCollection());
        sortColl.sort(Comparator.comparing(Pen::getWeight));

        return sortColl;
    }

    /**
     * находит элементы с одинаковыми id
     */
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
