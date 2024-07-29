package shared.request;

import shared.Model.Skill;
import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("PurchaseSkillRequest")

public class PurchaseSkillRequest implements Request{
    private String macAddress;
    private Skill skill;

    public PurchaseSkillRequest(String macAddress, Skill skill) {
        this.macAddress = macAddress;
        this.skill = skill;
    }

    public PurchaseSkillRequest() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handlePurchaseSkillRequest(this);
    }
}
