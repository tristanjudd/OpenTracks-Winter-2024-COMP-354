package de.dennisguse.opentracks.ui.aggregatedStatistics;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.stats.MockupData;
import de.dennisguse.opentracks.stats.TrackStatistics;

public class AggregatedStatistics {

    private final Map<String, AggregatedStatistic> dataMap = new HashMap<>();

    private final List<AggregatedStatistic> dataList = new ArrayList<>();

    public AggregatedStatistics(@NonNull List<Track> tracks) {
        for (Track track : tracks) {
            aggregate(track);
        }
        MockupData mockupData = new MockupData();
        List<TrackStatistics> trackStatistics = mockupData.getTrackStatistics();
        //int getTotalRuns = trackStatistics.get(0).getTotalRunsSeason();
        //int getTotalRuns2 = trackStatistics.get(1).getTotalRunsSeason();
        dataList.addAll(dataMap.values());
        dataList.sort((o1, o2) -> {
            if (o1.getCountTracks() == o2.getCountTracks()) {
                return o1.getActivityTypeLocalized().compareTo(o2.getActivityTypeLocalized());
            }
            return (o1.getCountTracks() < o2.getCountTracks() ? 1 : -1);
        });
    }

    @VisibleForTesting
    public void aggregate(@NonNull Track track) {
        String activityTypeLocalized = track.getActivityTypeLocalized();
        if (dataMap.containsKey(activityTypeLocalized)) {
            dataMap.get(activityTypeLocalized).add(track.getTrackStatistics());
        } else {
            dataMap.put(activityTypeLocalized, new AggregatedStatistic(activityTypeLocalized, track.getTrackStatistics()));
        }
    }

    public int getCount() {
        return dataMap.size();
    }

    public AggregatedStatistic get(String activityType) {
        return dataMap.get(activityType);
    }

    public AggregatedStatistic getItem(int position) {
        return dataList.get(position);
    }

    public static class AggregatedStatistic {
        private final String activityTypeLocalized;
        private final TrackStatistics trackStatistics;
        private int countTracks = 1;

        // My code
        private ArrayList<TrackStatistics> listOfTracks;

        public AggregatedStatistic(String activityTypeLocalized, TrackStatistics trackStatistics) {
            this.activityTypeLocalized = activityTypeLocalized;
            this.trackStatistics = trackStatistics;

            // My code
            this.listOfTracks = new ArrayList<TrackStatistics>();
            this.listOfTracks.add(trackStatistics);

        }

        public String getActivityTypeLocalized() {
            return activityTypeLocalized;
        }

        public TrackStatistics getTrackStatistics() {
            return trackStatistics;
        }

        public int getCountTracks() {
            return countTracks;
        }

        void add(TrackStatistics statistics) {
            trackStatistics.merge(statistics);
            countTracks++;

            // My code
            listOfTracks.add(statistics);
        }

        // My code
        public ArrayList<TrackStatistics> getListOfTracks() {return listOfTracks;}
    }
}
