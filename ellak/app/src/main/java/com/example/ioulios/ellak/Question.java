package com.example.ioulios.ellak;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Question {

	private static int quantityQuestestionsObj = 0 ; //Counter

	private int questionNumber  = 0 ;
	private int baseID  = 0 ; //No used
	private String questionText ;
	private String[] answerTexts ;
	private boolean[] correctAnswers ;
	private int userAnswerIndex = -1 ;

	private Context myParentContext ;
	private String imagePath ;
	private Bitmap questionImage ;

	//Constructor :
	public Question(Context _myParentContext)
	{
		myParentContext = _myParentContext ;

		questionNumber = ++quantityQuestestionsObj ;

		fullRandom() ;
	}
	//Constructor : END

	//Destructor :
	protected void finelize()
	{
		quantityQuestestionsObj-- ;
	}
	//Destructor : END

	//set methods :
	public void setUserAnswer(int index)
	{
		if (index >= answerTexts.length)
			userAnswerIndex = answerTexts.length-1 ;
		else if (index < -1)
			userAnswerIndex = -1 ;
		else
			userAnswerIndex = index ;
	}

	public void setQuestionNumber(int index)
	{
		questionNumber = index ;
	}
	//set methods : END

	//get methods :
	public int getUserAnswerIndex()
	{
		return userAnswerIndex ;
	}

	public int getQuestionNumber()
	{
		return questionNumber ;
	}

	public Bitmap getQuestionImage()
	{
		return questionImage ;
	}

	public String getQuestionText()
	{
		return questionText;
	}

	public String[] getAnswerTexts()
	{
		return answerTexts ;
	}

	public boolean [] getCorrectAnswers()
	{
		return correctAnswers ;
	}
	//get methods : END

	//test generator :
	private static final String[] IMAGESPATHS = {"image1.png","image2.png","image3.png","image4.png"} ;
	public void fullRandom()
	{
		userAnswerIndex = -1 ;
		questionText = makeRandomText();

		Random random = new Random();

		imagePath = IMAGESPATHS[random.nextInt(4)] ;

		questionImage = random.nextInt(3) == 0 ? null : getBitmapFromAsset(myParentContext,"B02.jpg"/*imagePath*/);

		int totalAnswers = random.nextInt(4)+2 ;
		answerTexts = new String[totalAnswers] ;

		correctAnswers = new boolean[totalAnswers];

		int randomTotalCorrectAnswers  = 0 ;

		if (totalAnswers > 3 )
			randomTotalCorrectAnswers = random.nextInt(2) ;

		boolean tmpCorAnswer ;
		for (int i = totalAnswers-1; i >= 0; i--) {
			answerTexts[i] = makeRandomText();

			if (randomTotalCorrectAnswers >= 0 )
			{
				if (i==randomTotalCorrectAnswers)
					tmpCorAnswer = true ;
				else
					tmpCorAnswer = random.nextBoolean();

				if (tmpCorAnswer)
					randomTotalCorrectAnswers-- ;
			}
			else
				tmpCorAnswer = false ;

			if (tmpCorAnswer)
				answerTexts[i] = "CORRECT! " + answerTexts[i] ;
			else
				answerTexts[i] = "MISTAKE! " + answerTexts[i] ;

			correctAnswers[i] = tmpCorAnswer ;
		}
	}
	//test generator : END

	//tools :
	public static Bitmap getBitmapFromAsset(Context context, String filePath) {
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
	//tools : END


	//test tools :
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
	//test tools : END
}

