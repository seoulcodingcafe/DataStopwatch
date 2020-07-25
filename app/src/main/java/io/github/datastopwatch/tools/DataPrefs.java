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

package io.github.datastopwatch.tools;

import com.pixplicity.easyprefs.library.Prefs;

public class DataPrefs {
	public boolean getIsStart() {
		return Prefs.getBoolean("is start", false);
	}

	public long getStartBytes() {
		return Prefs.getLong("start bytes", 0);
	}

	public long getStartTime() {
		return Prefs.getLong("start time", 0);
	}

	public long getTotalBytes() {
		return Prefs.getLong("total bytes", 0);
	}

	public long getTotalTime() {
		return Prefs.getLong("total time", 0);
	}

	public void setIsStart(boolean isStart) {
		Prefs.putBoolean("is start", isStart);
	}

	public void setStartBytes(long startBytes) {
		Prefs.putLong("start bytes", startBytes);
	}

	public void setStartTime(long startTime) {
		Prefs.putLong("start time", startTime);
	}

	public void setTotalBytes(long totalBytes) {
		Prefs.putLong("total bytes", totalBytes);
	}

	public void setTotalTime(long totalTime) {
		Prefs.putLong("total time", totalTime);
	}

}
