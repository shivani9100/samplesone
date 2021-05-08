package com.Inventory.Project.AssectService.MaillingService;

public class Mail {

	private String mailFrom;

	private String mailTo;

	private String[] mailToArray;

	private String mailCc;

	private String[] mailCcArray;

	private String mailBcc;

	private String mailSubject;

	private String mailContent;

	private String contentType;

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String[] getMailToArray() {
		return mailToArray;
	}

	public void setMailToArray(String[] mailToArray) {
		this.mailToArray = mailToArray;
	}

	public String[] getMailCcArray() {
		return mailCcArray;
	}

	public void setMailCcArray(String[] mailCcArray) {
		this.mailCcArray = mailCcArray;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public String getMailBcc() {
		return mailBcc;
	}

	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	// private List < Object > attachments;

}
