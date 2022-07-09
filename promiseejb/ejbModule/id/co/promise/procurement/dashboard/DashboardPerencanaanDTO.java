package id.co.promise.procurement.dashboard;

import java.math.BigDecimal;

public class DashboardPerencanaanDTO {
	
		private int year;
		private int month;
		private int week;
		private long needVerification;
		private long onProgress;
		private long received;
		private long procurementProcess;
		private long approvalProcess;
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getMonth() {
			return month;
		}
		public void setMonth(int month) {
			this.month = month;
		}
		public int getWeek() {
			return week;
		}
		public void setWeek(int week) {
			this.week = week;
		}
		public long getNeedVerification() {
			return needVerification;
		}
		public void setNeedVerification(long needVerification) {
			this.needVerification = needVerification;
		}
		public long getOnProgress() {
			return onProgress;
		}
		public void setOnProgress(long onProgress) {
			this.onProgress = onProgress;
		}
		public long getReceived() {
			return received;
		}
		public void setReceived(long received) {
			this.received = received;
		}
		public long getProcurementProcess() {
			return procurementProcess;
		}
		public void setProcurementProcess(long procurementProcess) {
			this.procurementProcess = procurementProcess;
		}
		public long getApprovalProcess() {
			return approvalProcess;
		}
		public void setApprovalProcess(long approvalProcess) {
			this.approvalProcess = approvalProcess;
		}
		
}
