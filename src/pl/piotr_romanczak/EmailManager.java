package pl.piotr_romanczak;

import pl.piotr_romanczak.controller.services.FetchFoldersService;
import pl.piotr_romanczak.controller.services.FolderUpdaterService;
import pl.piotr_romanczak.model.EmailAccount;
import pl.piotr_romanczak.model.EmailMessage;
import pl.piotr_romanczak.model.EmailTreeItem;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private FolderUpdaterService folderUpdaterService;
    //Folder handling:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    public EmailTreeItem<String> getFoldersRoot(){
        return foldersRoot;
    }

    private List<Folder> folderList = new ArrayList<Folder>();
    public  List<Folder> getFolderList(){
        return this.folderList;
    }

    public EmailManager(){
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public void addEmailAccount(EmailAccount emailAccount){
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem, folderList);
        fetchFoldersService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public void setRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        } catch (Exception e) {

        }
    }
}
