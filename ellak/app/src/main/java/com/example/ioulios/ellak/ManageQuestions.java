package com.example.ioulios.ellak;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Random;

public class ManageQuestions {

	public enum CustomType {
		EDUCATION, EXAM ;
	}

	public static int ALLQUESTIONS = 20  ;

	private Question[] arrayWithQuestions ;
	private int index  = -1 ;
	private Question curQuestion ; //Current Question
	private int remaining = ALLQUESTIONS ;
	private CustomType currentType ;

	private Context myParentContext ;
	//Constructor :
	public ManageQuestions(Context _myParentContext)
	{
		myParentContext = _myParentContext ;

		arrayWithQuestions = new Question[ALLQUESTIONS] ;

		for (int i=0 ; i<ALLQUESTIONS;i++)
			arrayWithQuestions[i] = new Question(myParentContext) ;

		index = 0 ;
		curQuestion = arrayWithQuestions[0] ;

		currentType = (new Random().nextBoolean()) ? CustomType.EDUCATION : CustomType.EXAM ;
	}
	//Constructor : END

	//Operations :

	public boolean next() //Return status : true if curQuestion go to the next
	{
		return next(true,true) ;
	}
	public boolean next(boolean overTake)
	{
		return next(overTake,true) ;
	}
	public boolean next(boolean overTake,boolean endNonStop)
    {
        if (overTake && remaining == 0)
            return false ;

        int tmpIndex = index ;

        if (overTake)
        {
			do
			{
				if ( ++tmpIndex >= ALLQUESTIONS)
				{
					if (endNonStop )
						tmpIndex = 0 ;
					else
						return false ;
				}
				if (arrayWithQuestions[tmpIndex].getUserAnswerIndex() == -1)
				{
					index = tmpIndex ;
					curQuestion = arrayWithQuestions[index] ;
				}

			}while(arrayWithQuestions[tmpIndex].getUserAnswerIndex() != -1) ;

        }
        else
        {
			if ( ++tmpIndex >= ALLQUESTIONS)
			{
				if (endNonStop)
					tmpIndex = 0 ;
				else
					return false ;
			}
			index = tmpIndex ;
			curQuestion = arrayWithQuestions[index] ;
		}

        return true ;
    }

	public boolean previous()//Return status : true if curQuestion go to the previous
	{
		return previous(true,true) ;
	}
	public boolean previous(boolean overTake)
	{
		return previous(overTake, true) ;
	}
	public boolean previous(boolean overTake,boolean beginNonStop)
	{
		if (overTake && remaining == 0)
			return false ;

		int tmpIndex = index ;

		if (overTake)
		{
			do
			{
				if ( --tmpIndex < 0)
				{
					if (beginNonStop )
						tmpIndex = ALLQUESTIONS - 1 ;
					else
						return false ;

				}
				if (arrayWithQuestions[tmpIndex].getUserAnswerIndex() == -1)
				{
					index = tmpIndex ;
					curQuestion = arrayWithQuestions[index] ;
				}

			}while(arrayWithQuestions[tmpIndex].getUserAnswerIndex() != -1) ;
		}
		else
		{
			if ( --tmpIndex < 0)
			{
				if (beginNonStop )
					tmpIndex = ALLQUESTIONS - 1 ;
				else
					return false ;
			}
			index = tmpIndex ;
			curQuestion = arrayWithQuestions[index] ;
		}

		return true ;
	}

	public void changeCurQuestion(int _index)
	{
		if (_index >= ALLQUESTIONS)
			index = ALLQUESTIONS -1 ;
		else if (_index < 0)
			index = 0 ;
		else
			index = _index;

		curQuestion = arrayWithQuestions[index];
	}
	//Operations : END

	//set methods :
	public void setCurrentType(CustomType type)
	{
		currentType = type ;
	}

	public void setUserResponseToCurQuestion(int answerIndex) //-1 for Cancel
	{
		if (answerIndex != -1)
			remaining -=  curQuestion.getUserAnswerIndex() == -1 ? 1 : 0 ;
		else
			remaining -=  curQuestion.getUserAnswerIndex() == -1 ? 0 : -1 ;
		curQuestion.setUserAnswer(answerIndex);
	}
	//set methods : END

	//get methods :

	public int getUserResponseIndexToCurQuestion()
	{
		return curQuestion.getUserAnswerIndex();
	}

	public int getCurQuestionNumber()
	{
		return curQuestion.getQuestionNumber() ;
	}

	public Bitmap getCurQuestionImage()
	{
		return curQuestion.getQuestionImage() ;
	}

	public String getCurQuestionText()
	{
		return curQuestion.getQuestionText();
	}

	public String[] getCurAnswerTexts()
	{
		return curQuestion.getAnswerTexts() ;
	}
	public boolean [] getCurCorrectAnswers()
	{
		return curQuestion.getCorrectAnswers() ;
	}
	public CustomType getCurrentType()
	{
		return currentType ;
	}

	///get methods - Future :
	public int getScore()
	{
		int right = 0 ;
		for (int i = 0 ;i<ALLQUESTIONS;i++)
		{
			for (int j = 0 ; j< arrayWithQuestions[i].getCorrectAnswers().length;j++)
				if (arrayWithQuestions[i].getCorrectAnswers()[j] && j == arrayWithQuestions[i].getUserAnswerIndex())
				{
					right += 1 ;
					break ;
				}
		}
		return Math.round(((right) / (float) (ALLQUESTIONS)) * 100) ;
	}

	public String getScoreEducation()
	{
		int right = 0 ;
		for (int i = 0 ;i<ALLQUESTIONS;i++)
		{
			for (int j = 0 ; j< arrayWithQuestions[i].getCorrectAnswers().length;j++)
				if (arrayWithQuestions[i].getCorrectAnswers()[j] && j == arrayWithQuestions[i].getUserAnswerIndex())
				{
					right += 1 ;
					break ;
				}
		}
		return ("(" + (right) + "/" + (ALLQUESTIONS - remaining) + ")") ;
	}

	public Object getResults()
	{
		//Future will be implemented

		return this ;
	}

	 /*
	public Question[] getArrayWithQuestions()
	{
		return arrayWithQuestions ;
	}
	*/
	///get methods - Future : END
	//get methods : END

	//Status :
	public boolean haveFinishedQuestions()
	{
		if (remaining == 0)
			return true ;
		else
			return false ;
	}
	//Status : END

	//test Tools :
	public void randomPrepare()
	{
		remaining = ALLQUESTIONS ;
		index = 0 ;
		curQuestion = arrayWithQuestions[index] ;
		for (int i = 0 ; i<ALLQUESTIONS;i++)
		{
			arrayWithQuestions[i].fullRandom();
			arrayWithQuestions[i].setQuestionNumber(i+1);
		}
	}
	//test Tools : END
}
