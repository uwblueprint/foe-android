package com.blueprint.foe.beetracker.Model;

/**
 * Represents the submission that is returned from the server. It extends Submission with two
 * properties, the id and the S3 image url.
 */
public class CompletedSubmission extends Submission {
    private int mId; // for de-duping
    private String mImageUrl;
    private static final String TAG = Submission.class.toString();


    @Override
    public void reset() {
        super.reset();
        mId = -1;
        mImageUrl = null;
    }

    @Override
    public boolean isComplete() {
        return super.isComplete() && mId != -1 && mImageUrl != null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CompletedSubmission)) {
            return false;
        }
        if (other == this) return true;

        CompletedSubmission that = (CompletedSubmission) other;

        return super.equals(that)
                && this.mId == that.mId
                && (this.mImageUrl == null && that.mImageUrl == null)
                    || (this.mImageUrl != null && this.mImageUrl.equals(that.mImageUrl));

//        return this.mSpecies == that.mSpecies
//                && this.mSpeciesType == that.mSpeciesType
//                && this.mHabitat == that.mHabitat
//                && this.mWeather == that.mWeather
//                && this.mId == that.mId
//                && this.mImageUrl == that.mImageUrl
//                && ((this.mDate == null && that.mDate == null)
//                || this.mDate != null && that.mDate != null && this.mDate.equals(that.mDate))
//                && ((this.mPlace == null && that.mPlace == null)
//                || this.mPlace != null && that.mPlace != null && this.mPlace.equals(that.mPlace));
    }
}
