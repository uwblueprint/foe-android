package com.blueprint.foe.beetracker.Model;

/**
 * Represents the submission that is returned from the server. It extends Submission with two
 * properties, the id and the S3 image url.
 */
public class CompletedSubmission extends Submission {
    private static final String TAG = Submission.class.toString();
    private int mId; // for de-duping
    private String mImageUrl;

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
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String url) {
        mImageUrl = url;
    }
}
