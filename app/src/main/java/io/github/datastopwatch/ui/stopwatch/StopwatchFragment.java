//    The GNU General Public License does not permit incorporating this program
//    into proprietary programs.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.datastopwatch.ui.stopwatch;

import android.annotation.SuppressLint;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.NumberFormat;

import io.github.datastopwatch.R;
import io.github.datastopwatch.tools.DataPrefs;

public class StopwatchFragment extends Fragment {

	private TextView mTextViewData;
	private TextView mTextViewTime;
	private DataPrefs mDataPrefs;

	private void checkError() {
		if (mDataPrefs.getStartTime() > SystemClock.elapsedRealtime()
				|| mDataPrefs.getStartBytes() > (TrafficStats.getTotalRxBytes() + TrafficStats.getMobileTxBytes()))
			reset();
	}

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_stopwatch, container, false);
		mTextViewData = root.findViewById(R.id.textViewData);
		mTextViewTime = root.findViewById(R.id.textViewTime);
		Button buttonStartOrStop = root.findViewById(R.id.buttonStartOrStop);
		buttonStartOrStop.setOnClickListener(v -> {
			startOrStop();
		});
		Button buttonReset = root.findViewById(R.id.buttonReset);
		buttonReset.setOnClickListener(v -> {
			reset();
		});
		mDataPrefs = new DataPrefs();
		checkError();
		print();
		return root;
	}

	@SuppressLint("SetTextI18n")
	private void print() {
		new Thread(() -> {
			long bytes = mDataPrefs.getTotalBytes();
			long time = mDataPrefs.getTotalTime();
			if (mDataPrefs.getIsStart()) {
				if (Prefs.getBoolean("mobile only", false))
					bytes += (TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes())
							- mDataPrefs.getStartBytes();
				else
					bytes += (TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes())
							- mDataPrefs.getStartBytes();

				time += SystemClock.elapsedRealtime() - mDataPrefs.getStartTime();
			}
			long finalBytes = bytes;
			long finalTime = time;
			mTextViewData.post(() -> {
				NumberFormat nf = NumberFormat.getIntegerInstance();
				mTextViewData.setText(nf.format(finalBytes / 1024) + " " + getString(R.string.KB));
			});
			mTextViewTime.post(() -> {
				mTextViewTime.setText(DateUtils.formatElapsedTime(finalTime / 1000));
			});
			if (mDataPrefs.getIsStart()) {
				try {
					Thread.sleep(100);
					print();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void reset() {
		mDataPrefs.setTotalBytes(0);
		mDataPrefs.setTotalTime(0);
		setStarts();
		print();
	}

	private void setStarts() {
		if (Prefs.getBoolean("mobile only", false))
			mDataPrefs.setStartBytes(TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes());
		else
			mDataPrefs.setStartBytes(TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes());
		mDataPrefs.setStartTime(SystemClock.elapsedRealtime());
	}

	private void setTotals() {
		if (Prefs.getBoolean("mobile only", false))
			mDataPrefs.setTotalBytes(
					mDataPrefs.getTotalBytes() + ((TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes())
							- mDataPrefs.getStartBytes()));
		else
			mDataPrefs.setTotalBytes(mDataPrefs.getTotalBytes()
					+ ((TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()) - mDataPrefs.getStartBytes()));

		mDataPrefs
				.setTotalTime(mDataPrefs.getTotalTime() + (SystemClock.elapsedRealtime() - mDataPrefs.getStartTime()));
	}

	private void startOrStop() {
		if (!mDataPrefs.getIsStart())
			setStarts();
		else
			setTotals();
		mDataPrefs.setIsStart(!mDataPrefs.getIsStart());
		print();
	}

}