public class Free extends Membership {
    final Integer dailyReadingLimitMinutes = 60;
    final Integer adsFrequencyMinutes = 15;

    public Free() {
        super("Free", 0.0, 2, 7, 0, false, true);
    }

    public boolean checkReadingLimit(Integer minutesRead) {
        return minutesRead != null && minutesRead < this.dailyReadingLimitMinutes;
    }

    public boolean shouldShowAd(Integer minutesElapsed) {
        return minutesElapsed != null && minutesElapsed > 0 && (minutesElapsed % this.adsFrequencyMinutes == 0);
    }

    public Integer getDailyReadingLimitMinutes() {
        return dailyReadingLimitMinutes;
    }

    public Integer getAdsFrequencyMinutes() {
        return adsFrequencyMinutes;
    }
}
