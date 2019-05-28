package e.group.thesismanager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public abstract class AbstractDocumentSubmission {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    @Value("${uploadedFile_type}")
    private String FILE_TYPE;

    @Value("${comment_maxCharCount}")
    private String MAX_COMMENT_CHAR;

    protected Boolean isCommentAcceptable(String comment) {
        return (comment.length() <= Long.valueOf(MAX_COMMENT_CHAR));
    }

    protected String getFileErrorMessageIfNotAcceptable(MultipartFile file) {

        if (file.isEmpty())
            return null;

        if (!Arrays.asList(FILE_TYPE.split(",")).contains(file.getContentType()))
            return " File type is not acceptable. Accepted types: " + FILE_TYPE;
        if (file.getSize() > Long.valueOf(MAX_FILE_SIZE.split("MB")[0])*1048576)
            return " File size is more than maximum acceptable size " + MAX_FILE_SIZE;

        return null;
    }

    protected String getCommentErrorMessageIfNotAcceptable(String comment) {
        if((comment.length() > Long.valueOf(MAX_COMMENT_CHAR)))
            return "Number of comment's characters cannot be more than " +
                MAX_COMMENT_CHAR + ". Character counts: " + comment.length();
        else
            return null;
    }
}
