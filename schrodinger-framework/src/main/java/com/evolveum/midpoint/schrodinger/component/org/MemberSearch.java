package com.evolveum.midpoint.schrodinger.component.org;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.MidPoint;
import com.evolveum.midpoint.schrodinger.component.assignmentholder.AssignmentHolderObjectListTable;
import com.evolveum.midpoint.schrodinger.component.common.search.Search;
import com.evolveum.midpoint.schrodinger.page.AssignmentHolderDetailsPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import com.evolveum.midpoint.schrodinger.util.Utils;

import static com.codeborne.selenide.Selenide.screenshot;
import static com.codeborne.selenide.Selenide.sleep;

public class MemberSearch<S> extends Search<S> {

    public MemberSearch(S parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public MemberSearch<S> byType(String type) {
        return byType(type, "Type");
    }

    public MemberSearch<S> byType(String type, String typeItemLabel) {
        dropDownPanelByItemName(typeItemLabel).inputDropDownValue(type);
        sleep(MidPoint.TIMEOUT_EXTRA_SHORT_1_S.toMillis());
        return this;
    }

    public MemberSearch<S> byRelation(String relation) {
        String translatedRelation = Utils.translate(relation);
        dropDownPanelByItemName("Relation").inputDropDownValue(translatedRelation);
        Selenide.sleep(MidPoint.TIMEOUT_DEFAULT_2_S.getSeconds());
        return this;
    }

    @Override
    public MemberSearch<S> updateSearch(){
        return (MemberSearch<S>) super.updateSearch();
    }
}
