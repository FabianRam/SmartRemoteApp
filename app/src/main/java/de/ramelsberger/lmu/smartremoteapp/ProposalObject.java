package de.ramelsberger.lmu.smartremoteapp;

/**
 * Created by Fabian on 14.01.2016.
 */
public class ProposalObject {

    private String proposalID;
    private String name;
    private String type;
    private String icon;

    public ProposalObject(String proposalID, String name, String type, String icon) {
        this.proposalID = proposalID;
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public String getProposalID() {
        return proposalID;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
