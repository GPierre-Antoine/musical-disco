package org.pag;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Launcher {
    public static void main(String[] args) {

        InputStream file = System.in;

        // on demande la taille du tableau à créer, entre 5 et 12
        int size = querySize(file, 5, 12);

        // on créé le tableau à partir de la taille précédemment demandée
        String[] monthes = buildMonthRawArray(size, "fr");

        // on affiche le contenu du tableau monthes
        printMonthArray(monthes);

        // on convertit le tableau brut précédemment créé en objet implémentant Collection<T>, en l'occurence via List<T>
        Collection<String> monthList = convertArrayToSortedCollection(monthes);

        // on affiche la collection
        printMonthCollection(monthList);
    }

    private static void printMonthCollection(Collection<String> monthList) {
        StringBuilder builder = new StringBuilder("La collection contient les éléments suivants :");
        buildStringFromCollection(monthList, builder);
        System.out.println(builder.append("\n").toString());
    }

    private static Collection<String> convertArrayToSortedCollection(String[] monthes) {
        // on convertit le tableau en List
        List<String> strings = Arrays.asList(monthes);
        // Plusieurs implémentations auraient été possibles, via par exemple un comparator
        // par exemple en passant par la fonction statique :
        //Collections.sort(monthList);
        strings.sort(String::compareTo);
        return strings;
    }

    private static void printMonthArray(String[] monthes) {
        // les strings étant immutable en java,
        // on génère le texte avant de l'imprimer d'un bloc à l'aide d'un StringBuilder
        StringBuilder builder = new StringBuilder("Le tableau contient les éléments suivants :");
        //on construit le texte associé aux mois. les tableaux n'étant pas des collections,
        // on ne peut pas rendre cette fonction générique aux Collections
        buildStringFromArray(monthes, builder);
        // on affiche la string générée
        System.out.println(builder.append("\n").toString());
    }

    private static String[] buildMonthRawArray(int size, String localeName) {
        // on créé le tableau de la taille demandée.
        // on aurait préféré une ArrayList dans un cadre normal, car les objets des Collections sont plus versatiles,
        // et généralement mieux utilisés avec les templates
        String[] monthes = new String[size];

        // on génère un objet capable de trouver le langage souhaité
        Locale locale = new Locale(localeName);

        // on définit le format d'affichage de dates, ici "LLLL" signifie "nom du mois de la date"
        SimpleDateFormat ffr = new SimpleDateFormat("LLLL", locale);

        // on récupère l'instance d'un calendrier grégorien, qu'on initialise
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // pour chaque case du tableau, on actualise le de la date, puis on récupère le nom du mois associé,
        // formatté correctement
        for (int i = 0; i < monthes.length; ++i) {
            calendar.set(Calendar.MONTH, i);
            String name = ffr.format(calendar.getTime());
            monthes[i] = capitalize(locale, name);
        }
        // on retourne les noms des mois.
        return monthes;
    }

    private static String capitalize(Locale locale, String name) {
        // on met la première lettre en majuscule et le reste en minuscule
        // en respectant les accents spécifiques à la langue.
        // étant donné que les noms sonts fournis en minuscule, il n'y a pas besoin de
        // transformer la seconde partie de la string
        return name.substring(0, 1).toUpperCase(locale) + name.substring(1);
    }

    private static void buildStringFromArray(String[] monthes, StringBuilder builder) {
        for (String month : monthes) {
            builder.append("\n - ").append(month);
        }
    }

    private static void buildStringFromCollection(Collection<String> monthes, StringBuilder builder) {
        for (String month : monthes) {
            builder.append("\n - ").append(month);
        }
    }

    private static int querySize(InputStream file, int minSize, int maxSize) {


        /*
         * ici j'ai choisis d'utiliser un Scanner plutot qu'un InputStreamReader,
         * dans la mesure où le scanner est directement équipé des méthodes d'analyse (pour parser)
         * en l'occurence, hasNextInt() et nextInt() ; plutot que de passer par Integer.parseInt()
         * qui lui lêve une exception en cas d'entrée incorrecte.
         */
        Scanner scanner = new Scanner(file);

        int size;

        // on affiche la requête à l'utilisateur
        System.out.println("Entrez un entier entre " + minSize + " et " + maxSize);


        String error = "Erreur : le nombre saisi doit-être compris entre " + minSize + " et " + maxSize;

        // on lit un entier
        size = readInteger(scanner);
        while (size < minSize || size > maxSize) {

            // on affiche la requête à l'utilisateur dans le flux d'erreur
            System.err.println(error);

            // on lit un entier
            size = readInteger(scanner);
            // si l'entier n'est pas compris entre min et max-sizes, on recommence.
        }

        // on retourne l'entier ainsi généré
        return size;
    }

    private static int readInteger(Scanner scanner) {
        // on recommence tant qu'un entier n'est pas fourni
        while (!scanner.hasNextInt()) {
            // on vide le buffer
            String text = scanner.next();
            // on affiche une erreur
            System.err.println(text + " n'est pas un entier !");
        }
        // on retourne l'entier
        return scanner.nextInt();
    }
}
