package it.unibo.bd18.stacklite.mapreduce.job2;

import it.unibo.bd18.util.TupleWritable;

public class TotalAnswersOutputValue extends TupleWritable {

    public static TotalAnswersOutputValue create(int questionCount, int totalAnswers) {
        return new TotalAnswersOutputValue(questionCount, totalAnswers);
    }

    public int questionCount() {
        return get(0);
    }

    public int totalAnswers() {
        return get(1);
    }

    public TotalAnswersOutputValue() {
        super();
    }

    private TotalAnswersOutputValue(int questionCount, int totalAnswers) {
        super(questionCount, totalAnswers);
    }
}
