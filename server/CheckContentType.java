package server;

/**
 * Created by connor on 07/11/2016.
 */
public class CheckContentType {

    String fileExtension;

    public CheckContentType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getContentType() {
        switch(fileExtension) {
            case "html":
                return "text/html";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpg";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "docx":
                return "application/msword";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
        }
        return "";
    }

}
