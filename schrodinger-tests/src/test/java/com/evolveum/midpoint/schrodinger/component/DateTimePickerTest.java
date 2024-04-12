package com.evolveum.midpoint.schrodinger.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.component.common.PrismForm;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.page.user.UserPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

public class DateTimePickerTest extends AbstractSchrodingerTest {

    private static final File TEST_USER = new File("./src/test/resources/objects/users/user.xml");

    private List<String> locales = new ArrayList<>();

    @BeforeClass(dependsOnMethods = {"springTestContextPrepareTestInstance"})
    @Override
    public void beforeClass() throws IOException {
        super.beforeClass();

        ElementsCollection links = $$(Schrodinger.byDataId("a", "localesLink"));
        for (SelenideElement link : links) {
            String clazz = link.$(Schrodinger.byDataId("i", "localesIcon")).getAttribute("class");
            if (StringUtils.isNotEmpty(clazz) && clazz.contains("fi-")) {
                clazz = clazz.substring(clazz.indexOf("fi-") + 3);
                String locale;
                if (clazz.contains(" ")) {
                    locale = clazz.substring(0, clazz.indexOf(" "));
                } else {
                    locale = clazz;
                }
                locales.add(locale);
            }
        }

    }

    @Override
    protected List<File> getObjectListToImport() {
        return Arrays.asList(TEST_USER);
    }

    @Test
    public void test010ShowDatePickerForAllLanguage() {
        UserPage page = showUser("ceresnickova");

        PrismForm<PanelWithContainerWrapper<UserPage>> panel = page
                .selectActivationPanel()
                .form()
                .showEmptyAttributes("ActivationType.activation");

        String currentLocale = "us";
        for (String locale : locales) {
            page.changeLanguageAfterLogin(currentLocale, locale);
            currentLocale = locale;

            panel.getDatePanel("validFrom").checkShowingOfPopover();
        }
        page.changeLanguageAfterLogin(currentLocale, "us");
    }

    @Test
    public void test020SetDateByDatePickerForAllLanguage() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        int prevYear = calendar.get(Calendar.YEAR);

        for (String locale : locales) {
            UserPage page = showUser("ceresnickova");
            PrismForm<PanelWithContainerWrapper<UserPage>> panel = page.selectActivationPanel()
                    .form()
                    .showEmptyAttributes("ActivationType.activation");

            page.changeLanguageAfterLogin("us", locale);

            panel.getDatePanel("validFrom")
                    .setDateTimeValueByPicker(
                            1,
                            1,
                            prevYear,
                            1,
                            10);

            FeedbackBox<? extends BasicPage> feedback = page.uncheckedFeedback();
            if (feedback.getParentElement().is(Condition.visible)) {
                assertion.assertFalse(page.feedback().isError(), "Feedback panel status contains error for language " + locale);
            }

            page.changeLanguageAfterLogin(locale, "us");
        }
    }


}
