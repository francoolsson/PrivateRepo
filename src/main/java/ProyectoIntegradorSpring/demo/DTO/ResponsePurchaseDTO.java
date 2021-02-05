package ProyectoIntegradorSpring.demo.DTO;

public class ResponsePurchaseDTO {

    private ReceiptDTO receipt;
    private StatusDTO statusCode;

    public ReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptDTO receipt) {
        this.receipt = receipt;
    }

    public StatusDTO getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusDTO statusCode) {
        this.statusCode = statusCode;
    }
}
