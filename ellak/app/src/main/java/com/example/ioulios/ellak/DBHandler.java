package com.example.ioulios.ellak;

import java.util.LinkedList;
import java.util.Random;

/**
 * � ����� DBHandler ���������� ����������� ��� �� ���� ��������� ��� ���������� ��������������. ������������� �������
 * ��� ��� ������������ �������� �� ������ ����� ����������� ��� ��� ���������� ����� ������� ��� ���������.
 * � ����� DBHandler ����� singleton.
 */
public class DBHandler
{

    private static DBHandler ourInstance = new DBHandler();

    /**
     * ���������� ������� ��� �������� ����������� ��� ������������� ��� ��� �����.
     * @return � ������� ��� �����������.
     */
    public static DBHandler getInstance ()
    {
        return ourInstance;
    }

    /**
     * � ������������� ��� ������������. ��� ������������� �� �������������� ��� ������������. ��� ��� ��� �����������
     * ������ �� ����� � ������� ������� ��� sqlite ������� ���� ������������ ���� ��� ��������� ��� � �������� ��� ���
     * �� assets �� ����������.
     */
    private DBHandler ()
    {

    }

    /**
     * ���������� ��� ����� �� �� �������� ����������� �� ������ ���������� ��� ���� ���������. �� �������� �����������
     * ������������� �� LinkedList �� ����������� {@link SubjectRec}.
     * @return � ����� �� ��� ���������� ��������.
     */
    public LinkedList <SubjectRec> GetKategories ()
    {
        return null;
    }

    /**
     * ���������� ��� ���������� ��� �������� ��������������. �� ��������� ����������� ������ ��� ��� ����������
     * ������������� ��� �� ���������� ���������� ��� ��������� ������������� ��, ������, ������ �����.
     * @param Subject � ������� ��� ��������� ������������ ��� ��������.
     * @return �� �������������� �� ����������� ��� ������ {@link TestSheet}.
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
     * ���������� ��� ������ ��� ��������� ������� ��� ����� ���������.
     * @return � �������� ������ ��� ����� ���������.
     */
    public float GetVersion ()
    {
        return 0.0f;
    }

}

/**
 * �������,�� record, ��� �������� ����������� ��������.
 */
class SubjectRec
{
    /**
     * � ������� ��� ��������� ������������ ��������.
     */
    public int SubjectID;
    /**
     * �� ������� (�����) ��� ��������� ������������.
     */
    public String SubjectName;
}

/**
 * �������, �� Record, ��� ������� ��� ���������������.
 */

class Question
{
    /**
     * � �������� ������� ��� �������� ��� ��������������
     */
    public int QNum;
    /**
     * �� ������� ��� ��������
     */
    public String QText;
    /**
     * �� ����� ��� ������� ������� �� ����� ����������� ���� ������� ("-" �� � ������� ��� ���� ������).
     */
    public String PicName;
    /**
     * ������� �� �� ������� ��� ����������. �� ������� ��� ������ ������� ��� �� ������ ��� ����������.
     */
    public String[] AText;
    /**
     * � ���� ��� ������ ��������� ���� ����������� ������
     */
    int CorAnswer;
}


/**
 * �������, �� Record, ��� �������� ��������������.
 */
class TestSheet
{
    /**
     * � ������� ��� ��������� ������������ ��� ���������������
     */
    public int SubjectID;
    /**
     * � ������ �������� �� ����� ����� ��� ����.
     */
    public int ExamTime;
    /**
     * ������� �� ��� ��������� ��� ���������������. ���� ������� ����� ��� ����������� ��� ������ {@link Question}
     */
    public Question[] Quests;
    /**
     * �� ������ ��� ��������� ��� ������ �� ���������� ����� ����������� � ������� �� �������� ��������.
     */
    int ReqCorAnswers;
}