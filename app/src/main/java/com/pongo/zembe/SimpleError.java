package com.pongo.zembe;

public class SimpleError {
  String status = Konstants.ERROR_PASSED ;
  String errorMessage=Konstants.INIT_STRING;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
