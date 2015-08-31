package com.example.ioulios.ellak;

import java.util.LinkedList;
import java.util.Random;

/**
 * Η κλάση DBHandler επιστρέφει πληροφορίες από τη βάση δεδομένων και δημιουργεί ερωτηματολόγια. Περιγράφονται κάποιες
 * από τις απαιτούμενες μεθόδους οι οποίες είναι απαραίτητες για την λειτουργία άλλων κλάσεων της εφαρμογής.
 * Η κλάση DBHandler είναι singleton.
 */
public class DBHandler
{

    private static DBHandler ourInstance = new DBHandler();

    /**
     * Επιστρέφει αναφορά στο μοναδικό αντικείμενο που δημιουργείται από την κλάση.
     * @return Η αναφορά στο αντικείμενο.
     */
    public static DBHandler getInstance ()
    {
        return ourInstance;
    }

    /**
     * Ο κατασκευαστής του αντικειμένου. Εδώ τοποθετούνται οι αρχικοποιήσεις του αντικειμένου. Μία από τις λειτουργίες
     * πρέπει να είναι ο έλεγχος ύπαρξης του sqlite αρχείου στον αποθεκευτικό χώρο της εφαρμογής και η μεταφορά του από
     * τα assets αν χρειάζεται.
     */
    private DBHandler ()
    {

    }

    /**
     * Επιστρέφει την λίστα με τα γνωστικά αντικείμενα τα οποίες βρίσκονται στη Βάση Δεδομένων. Τα γνωστικά αντικείμενα
     * επιστρέφονται ως LinkedList με αντικείμενα {@link SubjectRec}.
     * @return Η λίστα με τις κατηγορίες εξέτασης.
     */
    public LinkedList <SubjectRec> GetKategories ()
    {
        return null;
    }

    /**
     * Δημιουργεί και επιστρέφει ένα ολόκληρο ερωτηματολόγιο. Οι ερωτήσεις επιλέγονται τυχαία από τις διαθέσιμες
     * υποκατηγορίες και οι διαθέσιμες απαντήσεις των ερωτήσεων τοποθετούνται με, επίσης, τυχαία σειρά.
     * @param Subject Ο κωδικός του γνωστικού αντικειμένου της εξέτασης.
     * @return Το ερωτηματολόγιο ως στιγμιότυπο της κλάσης {@link TestSheet}.
     */
    public TestSheet CreateTestSheet (int Subject)
    {
        TestSheet tsTmp = new TestSheet() ;

        tsTmp.SubjectID = Subject ;

        if (new Random().nextInt(4) == 0)
        {
            tsTmp.ExamTime = 35 ;
            tsTmp.ReqCorAnswers = 20 ;
        }
        else
        {
            tsTmp.ExamTime = 10 ;
            tsTmp.ReqCorAnswers = 10 ;
        }
        tsTmp.Quests = new Question[tsTmp.ReqCorAnswers] ;
        for (int i = 0 ; i<tsTmp.ReqCorAnswers;i++)
        {
            tsTmp.Quests[i] = new Question() ;
            tsTmp.Quests[i].QNum = i+1 ;
        }

        for (Question quest : tsTmp.Quests)
            fullRandom(quest);

        return tsTmp ;
    }
    private static final String[] IMAGESPATHS = {"image1.png","image2.png","image3.png","image4.png","B02.jpg"} ;
    public void fullRandom(Question quest)
    {
        quest.QText = makeRandomText();

        Random random = new Random();

        quest.PicName = IMAGESPATHS[random.nextInt(4)] ;

        int totalAnswers = random.nextInt(4)+2 ;
        quest.AText = new String[totalAnswers] ;

        boolean tmpCorAnswer ,thereIsCorrectAnswer = false ;
        for (int i = totalAnswers-1; i >= 0; i--) {
            quest.AText[i] = makeRandomText();

            tmpCorAnswer = random.nextBoolean();
            if (i==0 && !thereIsCorrectAnswer)
                tmpCorAnswer = true ;

            if (tmpCorAnswer && !thereIsCorrectAnswer)
            {
                quest.AText[i] = "CORRECT! " + quest.AText[i] ;
                quest.CorAnswer = i ;
                thereIsCorrectAnswer = true ;
            }
            else
                quest.AText[i] = "MISTAKE! " + quest.AText[i] ;
        }
    }
    private static final String ABC =  "abcdefghijklmnopqrstuvwxyz     " ;
    private String makeRandomText()
    {
        char[] chars = ABC.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int tmpLen = random.nextInt(150)+20 ;
        for (int i = 0; i < tmpLen ; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString() ;
    }

    /**
     * Επιστρέφει την έκδοση της τρέχουσας έκδοσης της βάσης δεδομένων.
     * @return Η τρέχουσα έκδοση της βάσης δεδομένων.
     */
    public float GetVersion ()
    {
        return 0.0f;
    }

}

/**
 * Παριστά,ως record, ένα γνωστικό αντικείμενο εξέτασης.
 */
class SubjectRec
{
    /**
     * Ο κωδικός του γνωστικού αντικειμένου εξέτασης.
     */
    public int SubjectID;
    /**
     * Το λεκτικό (όνομα) του γνωστικού αντικειμένου.
     */
    public String SubjectName;
}

/**
 * Παριστά, ως Record, μία ερώτηση του ερωτηματολογίου.
 */

class Question
{
    /**
     * Ο Αύξωντας Αριθμός της Ερώτησης στο ερωτηματολόγιο
     */
    public int QNum;
    /**
     * Το κείμενο της ερώτησης
     */
    public String QText;
    /**
     * Το όνομα του αρχείου εικόνας το οποίο αντιστοιχεί στην ερώτηση ("-" αν η ερώτηση δεν έχει εικόνα).
     */
    public String PicName;
    /**
     * Πίνακας με τα κείμενα των απαντήσεων. Το μέγεθος του πίνακα δηλώνει και το πλήθος των απαντήσεων.
     */
    public String[] AText;
    /**
     * Η θέση της σωστής απάντησης στον προηγούμενο πίνακα
     */
    int CorAnswer;
}


/**
 * Παριστά, ως Record, ένα ολόκληρο ερωτηματολόγιο.
 */
class TestSheet
{
    /**
     * Ο κωδικός του γνωστικού αντικειμένου του ερωτηματολογίου
     */
    public int SubjectID;
    /**
     * Ο χρόνος εξέτασης σε πρώτα λεπτά της ώρας.
     */
    public int ExamTime;
    /**
     * Πίνακας με τις ερωτήσεις του ερωτηματολογίου. Κάθε ερώτηση είναι ένα αντικείμενο της κλάσης {@link Question}
     */
    public Question[] Quests;
    /**
     * Το πλήθος των ερωτήσεων που πρέπει να απαντηθούν σωστά προκειμένου η εξέταση να θεωρηθεί επιτυχής.
     */
    int ReqCorAnswers;
}