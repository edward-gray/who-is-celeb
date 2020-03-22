package com.example.guesstheceleberity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView nameTextView;
    List<ImageView> photoImageViews;
    Button nextButton;

    String html;
    List<Celebrity> celebrities;
    int currentPositionInList;
    ImageView correctImageView;

    public void handleNext(View view) {
        currentPositionInList++;
        reset();
        generate();
    }

    public void handleAnswer(View view) {
        ImageView imageView = (ImageView) view;
        if (imageView.getTag() == "correct") {
            imageView.setBackgroundColor(Color.CYAN);
        } else {
            imageView.setBackgroundColor(Color.RED);
            correctImageView.setBackgroundColor(Color.CYAN);
        }
        imageViewsClickable(false);
        nextButton.setVisibility(View.VISIBLE);
    }

    public List<ImageView> getImageViews() {
        List<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView imageView = (ImageView) row.getChildAt(j); // get child index on particular row
                imageViews.add(imageView);
            }
        }
        return imageViews;
    }

    private void imageViewsClickable(boolean clickable) {
        for (ImageView imageView : photoImageViews) {
            imageView.setClickable(clickable);
        }
    }

    private void generate() {
        // getting random position for correct answer
        Random random = new Random();
        int correctPos = random.nextInt(photoImageViews.size());

        int celebNum = celebrities.size();

        for (int i = 0; i < photoImageViews.size(); i++) {
            if (i == correctPos) {
                // rendering correct position of celeb
                photoImageViews.get(i).setImageBitmap(celebrities.get(currentPositionInList).getBitmap());
                photoImageViews.get(i).setTag("correct");
                correctImageView = photoImageViews.get(i);

                // rendering question
                nameTextView.setText(celebrities.get(currentPositionInList).getName());
            } else {
                // finding random celeb that doesn't match correct celeb
                Celebrity wrongCeleb = celebrities.get(random.nextInt(celebNum));

                while (wrongCeleb.getName().equals(celebrities.get(currentPositionInList).getName())) {
                    wrongCeleb = celebrities.get(random.nextInt(celebNum));
                }
                photoImageViews.get(i).setImageBitmap(wrongCeleb.getBitmap());
                photoImageViews.get(i).setTag(wrongCeleb.getName());
            }
        }

    }

    public void reset() {
        nextButton.setVisibility(View.INVISIBLE);
        for (ImageView imageView : photoImageViews) {
            imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setClickable(true);
        }
        if (currentPositionInList == (celebrities.size()-1)) {
            currentPositionInList = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // defaults
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        photoImageViews = getImageViews();
        nextButton = (Button) findViewById(R.id.nextButton);

        nextButton.setVisibility(View.INVISIBLE);

        DownloadWebContent downloadWebContent = new DownloadWebContent();
        html = downloadWebContent.download("https://www.imdb.com/list/ls052283250/");

        celebrities = getListOfCelebrities();
        currentPositionInList = 0;

        generate();
    }

    private List<Celebrity> getListOfCelebrities() {
        List<Celebrity> celebrities = new ArrayList<>();

        Pattern pattern = Pattern.compile("<img alt=\"(.*?)\"\n" +
                "height=\"(.*?)\"\n" +
                "src=\"(.*?)\"\n" +
                "width=\"(.*?)\" />");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            celebrities.add(new Celebrity(matcher.group(3), matcher.group(1)));
        }
        return celebrities;
    }
}
