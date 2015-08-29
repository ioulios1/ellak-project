package com.example.ioulios.ellak;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import junit.framework.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
/**
 * � ����� ManageQuestions ���� ����� �� �������� ���� �������� ���������� ��� ��������� ��� �� ������ ��� �������������� ��� MainScreen activity .
 * ����������� ����������� ��� ���������� ��� ��������� ��� ��������� ��� MainScreen activity �� �������� ���� �� ��� �������� ��� �������� .
 * ����������� ��� �� ������ ��� ��������� ( ��� ��������� ��� ������ ���) �� 2 �������� ����� ���� �� ����� � ������� ��� ���������� ,��� �������������
 * ��� ��� ������ ���������� ����������� ...
 */
public class ManageQuestions {
	/**
	 * ����� ��� �� ��������� ��� Test .
	 */
	public enum TestType {
		EDUCATION, EXAM ;
	}
	/**
	 * ������� ��� ���������� ��� ������ ���� ��������� .
	 */
	private int[] userAnswerIndexs  ;
	/**
	 * �� ������� ��� ��������� .
	 */
	private Bitmap[] questionImages ;
	/**
	 * �� ������ ��� ��������� .
	 */
	public int allTotalQuestions ;
	/**
	 * � ������� ��� ������ �������� .
	 */
	private int index  ;
	/**
	 * ������� ����� ��������� ��������� ��� �� ��������� �� test .
	 */
	private int remaining ;
	/**
	 * ������� �� ���� ��� test .
	 */
	private TestType currentType ;
	/**
	 * ������� ��� Context �� ������� ����� �� "������" � ����� �� Assets .
	 * ������ ��� ����� ���� ���������� ��� (Constactor) .
	 */
	private Context myParentContext ;
	/**
	 * ���� ��� ��� ���������� ��� ��������� ��� Test ��� ������� ��� �� ���� ��������� .
	 */
	private TestSheet tsTest ;
	/**
	 * ���� ��� ���� �� ������������ ��� ������� �� test .
	 */
	private int secondsForTest ;
	/**
	 * ���� ��� ���� �� ������������ ��� ����������� ��� �� ����������� �� 75% ��� ������ ��� ���� �� test .
	 */
	private int seventyFivePercentOfsecondsForTest ;

	/**
	 * � ������������� ��� ������������ . ��������������� ��� ��� ������������ ��� ������������ ����������� �� �����
	 * ������ ��� �� �������������� ��� ��������� ...
	 * @param _myParentContext ������� ��� context �� ������� ����� �� "������" � ����� �� Assets.
	 * @param subject � ������� ��� ������� ��� �� test .
	 * @param testType � ������� ��� ����� ��� �� test .
	 */
	public ManageQuestions(Context _myParentContext,int subject,int testType)
	{
		myParentContext = _myParentContext ;

		tsTest = DBHandler.getInstance().CreateTestSheet(subject) ;

		allTotalQuestions = tsTest.ReqCorAnswers ;
		remaining = allTotalQuestions ;

		if (testType == 0)
			currentType = TestType.EDUCATION ;
		else
		{
			currentType = TestType.EXAM ;
			secondsForTest = tsTest.ExamTime * 60 ;
			seventyFivePercentOfsecondsForTest = (int)((secondsForTest) * 0.75)  ;
		}

		fillImagesAndAnswersArray(tsTest) ;
		index = 0 ;
	}


	/**
	 * �������� , ������� ��� �������� ������� �� ��� ������� �� ���������� ... (���������� ������� � ������� ��� ��������� ��������)
	 * @return ���������� true �� ������ � �������� ������� �� ��� ������ �� ���������� �������.� false �� ��� ������ � �������� ������� (����� ���� �� ��������� �����������)
	 */
	public boolean next()
	{
		if (remaining == 0)
			return false ;

		do
		{
			if ( ++index >= allTotalQuestions)
				index = 0 ;
		} while(userAnswerIndexs[index] != -1) ;

		return true ;
	}
	/**
	 * ����������� � �������� ��� ������ ���� �������� �������.
	 * @param answerIndex � �������� ��� ������ (� ������� ��� �� ������ ��� ���������� ��� ��������� ��������) .
	 */
	public void setUserResponseToCurQuestion(int answerIndex) //-1 for Cancel
	{
		if (answerIndex != -1)
			remaining -=  userAnswerIndexs[index] == -1 ? 1 : 0 ;
		else
			remaining -=  userAnswerIndexs[index]  == -1 ? 0 : -1 ;

		userAnswerIndexs[index] = answerIndex;
	}


	/**
	 * ���������� To ������� ������ ���������� ��� ���� ��� ��������� ��� test
	 * @return To ������� ������ ���������� .
	 */
	public int getMaxAnswers()
	{
		int max = 0 ;
		for (Question quest : tsTest.Quests)
			if (quest.AText.length > max)
				max = quest.AText.length ;

		return max ;
	}
	/**
	 * ���������� ��� �������� ��� ������ ��� ��� ��������� �������� .
	 * @return �������� ��� ������ (� ������� ��� �� ������ ��� ���������� ��� ��������� ��������).
	 */
	public int getUserResponseIndexToCurQuestion()
	{
		return userAnswerIndexs[index];
	}
	/**
	 * ���������� ��� ������ ��� ��������� ��������.
	 * @return ������� ������ ��������
	 */
	public int getCurQuestionNumber()
	{
		return tsTest.Quests[index].QNum ;
	}
	/**
	 * ���������� ��� ������ ��� �������� .
	 * @return ������ {@link Bitmap} � null �� ��� ���� ������ � ������ ������� .
	 */
	public Bitmap getCurQuestionImage()
	{
		return questionImages[index] ;
	}
	/**
	 * ���������� �� ������� ��� �������� .
	 * @return ������� ��� ������ �������� .
	 */
	public String getCurQuestionText()
	{
		return tsTest.Quests[index].QText ;
	}
	/**
	 * ���������� �� ������� ����� �� ����� ��� �� �������� ��� test (Exam) .
	 * @return ��������� ����� ��� �� test.
	 */
	public int getExamTime()
	{
		return tsTest.ExamTime ;
	}
	/**
	 * ���������� �� ������� ��� ���������� .
	 * @return T� ������� ��� ���������� ��� ������ ��������.
	 */
	public String[] getCurAnswerTexts()
	{
		return tsTest.Quests[index].AText ;
	}
	/**
	 * ���������� ��� ����� �������� .
	 * @return � ����� �������� ��� ��� ������ ������� (� ������� ��� �� ������ ��� ���������� ��� ��������� ��������).
	 */
	public int getCurCorrectAnswer()
	{
		return tsTest.Quests[index].CorAnswer ;
	}
	/**
	 * ���������� �� ������� ����� �� ������������ ��� �� �������� ��� test (Exam) .
	 * @return ��������� ������������ ��� �� test.
	 */
	public int getSecondsForTest()
	{
		return secondsForTest ;
	}
	/**
	 * ���������� �� ����� �� ������������ ��� ��� ���������� ��� 75% ��� test (Exam) .
	 * @return ������������ ��� ��� ���������� ��� 75% ��� test.
	 */
	public int getSeventyFivePercentOfsecondsForTest()
	{
		return seventyFivePercentOfsecondsForTest ;
	}
	/**
	 * ���������� �� ������ ���� ��� ��������� .
	 * @return ������ ��������� .
	 */
	public int getAllTotalQuestions()
	{
		return allTotalQuestions;
	}
	/**
	 * ���������� ��� ��������� ��� Test .
	 * @return ��������� ��� Test .
	 */
	public TestType getCurrentType()
	{
		return currentType ;
	}
	/**
	 * ���������� ��� ������� �� �� "score" ��� ������ example : "(3/7)" ������ ��� ��� �������� ����� ������� ����������� ���������.
	 * @return �� "score" ��� ������ .
	 */
	public String getScoreEducation()
	{
		int right = 0 ;
		for (int i = 0 ;i<allTotalQuestions;i++)
		{
			if (tsTest.Quests[i].CorAnswer == userAnswerIndexs[i])
				right++ ;

		}
		return ("(" + (right) + "/" + (allTotalQuestions - remaining) + ")") ;
	}

	/**
	 * ���������� ��� {@link Bundle} �� ��� ����������� ��� ����������� ��� �� ���������� ������������ ��� test .
	 * @return �� ���������� ������������ ��� test.
	 */
	public Bundle getBundleOfResults()
	{
		Bundle bu = new Bundle() ;

		bu.putInt("Subject",tsTest.SubjectID);
		bu.putInt("ExamTime",tsTest.ExamTime);
		bu.putInt("ReqCorAnswers",tsTest.ReqCorAnswers);

		int allAnsQuests, rightQuests, mistakeQuests ;
		allAnsQuests = rightQuests = mistakeQuests = 0 ;

		for (int i = 0 ;i<tsTest.Quests.length;i++)
			if (userAnswerIndexs[i] != -1)
				if (userAnswerIndexs[i] == tsTest.Quests[i].CorAnswer)
					rightQuests++ ;
				else
					mistakeQuests++ ;

		bu.putInt("allAnsQuests",tsTest.ReqCorAnswers - remaining);
		bu.putInt("rightQuests",rightQuests);
		bu.putInt("mistakeQuests",mistakeQuests);

		return bu ;
	}

	/** ���������� true �� ����� ��������� ���� �� ��������� � false �� ��� ����� ��������� ����
	 * @return ���������� ��� ��������� ����� {@link Boolean}.
	 */
	public boolean haveFinishedQuestions()
	{
		if (remaining == 0)
			return true ;
		else
			return false ;
	}

	/**
	 * �������-����������� ���� ������� ��� ������� ��� ���������� ��� ������ .
	 * @param tsTmp ��� ����������� ����� {@link TestSheet} ��� �� ����� �� path names ��� ������� ��� �� ������ ��� ��������� .
	 */
	private void fillImagesAndAnswersArray(TestSheet tsTmp)
	{
		userAnswerIndexs = new int[tsTmp.ReqCorAnswers] ;
		questionImages = new Bitmap[tsTmp.ReqCorAnswers] ;
		Random random = new Random();
		for (int i = 0 ; i< tsTmp.ReqCorAnswers; i++)
		{
			userAnswerIndexs[i] = -1 ;
			questionImages[i] = random.nextInt(3) == 0 ? null : getBitmapFromAsset(myParentContext,tsTmp.Quests[i].PicName);
		}
		//������ �� ������� � ��������� "getBitmapFromAsset" ��� �� ���������� ��� ������ ��� �� ����� ��� ��� ��� �� Assets .
	}

	/**
	 *  ���������� ��� ������ bitmap ��� �� Assets .
	 *   @param context  �� context ��� ���� �� Assets.
	 *   @param filePath �� �������� ��� ������� ��� Assets.
	 * 	 @return ��� ����������� ���� ������� ����� {@link Bitmap}.
	 */
	private static Bitmap getBitmapFromAsset(Context context, String filePath) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(filePath);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			// handle exception
		}

		return bitmap;
	}
}
