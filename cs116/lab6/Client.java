

public class Client {

    public static void main(String[] args) {
        Book book = new Book(new Record("Peterpan", "1999-10-2"), "jhon smith", "first edition");
        Newspaper np = new Newspaper(new Record("Japan attacks", "1939-12-7"), "FDR", "new york");
        EMagazine emag = new EMagazine(new Record("newtech", "2014-12-1"), 2, 33, "www.google.com/popsci", "popular science");


        System.out.println(book.toString() + "\n");
        System.out.println(np.toString() + "\n");
        System.out.println(emag.toString() + "\n");

    }
};
