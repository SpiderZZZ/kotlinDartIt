import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.open
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import kotlin.test.assertEquals

class DartItTest {

    @Before fun setUp() {
        open("http://www.dartit.ru/")
    }

    /*
    1.Открыть веб-сайт http://www.dartit.ru, валидация страницы по ключевым словам контента
    */
    @Test fun dartItMainPageTest() {
        assertEquals("DartIT",Selenide.title())
        Selenide.`$`(By.xpath("//header//img")).isImage
        Selenide.`$$`(By.xpath("//header//div[@class='row']//ul[contains(@class,'menu')]//li")).shouldHave(size(7))
        Selenide.`$`(By.xpath("//span[text()='Продукты']")).should(visible)
        Selenide.`$`(By.xpath("//span[text()='О компании']")).should(visible)
        Selenide.`$`(By.xpath("//span[text()='Контакты']")).should(visible)
        Selenide.`$`(By.xpath("//div[@class='header__phone']")).shouldHave(text(" +7 (342) 270-02-03"))
        val file = Selenide.`$`(By.xpath("//p[text()='Скачать презентацию']/../../..")).download()
        assertEquals("AMPLOS.pdf",file.name);
        Selenide.`$`(By.xpath("//h1[text()='Наши клиенты']/..//img")).isImage
        Selenide.`$`(By.xpath("//a[@href='/contacts?perm']")).shouldHave(text("Пермь, ул. Пушкина, 80, 4 этаж"))
        Selenide.`$`(By.xpath("//a[@href='/contacts?moscow']")).shouldHave(text("Москва, ул. Новый Арбат, 21, 23 этаж, офис 7"))
        Selenide.`$`(By.xpath("//a[@href='/contacts?ekaterinburg']")).shouldHave(text("Екатеринбург, ул. Малышева, 51, офис 1619, Бизнес центр \"Высоцкий\""))
    }

    /*
    2.Перейти на вкладку “Контакты”, валидация страницы по ключевым словам контента
    3.На вкладке “Контакты” проверить актуальность адреса офиса в Екатеринбурге: должно быть “ул. Малышева, 51, офис 1619”
    */
    @Test fun dartItContactPageTest() {
        Selenide.`$`(By.xpath("//span[text()='Контакты']")).click()
        assertEquals("Контакты",Selenide.title())
        Selenide.`$`(By.xpath("//div[contains(@class,'content-area')]//h1")).shouldHave(text("Наши контакты"))
        Selenide.`$$`(By.xpath("//ul[@id='city-ul']//li")).shouldHave(size(3));
        Selenide.`$`(By.id("perm")).exists();
        Selenide.`$`(By.id("moscow")).exists();
        Selenide.`$`(By.id("ekaterinburg")).exists();
        Selenide.`$`(By.xpath("//div[contains(@id,'ymaps-map-id')]/ymaps")).isDisplayed;
        Selenide.`$`(By.xpath("//h2[text()='Служба сопровождения и поддержки']")).isDisplayed;
        Selenide.`$`(By.xpath("//ul[@class='list-indent-icon list-icon-handset']//li")).shouldHave(text("+7 (922) 200-11-15"))
        Selenide.`$`(By.xpath("//ul[@class='list-indent-icon list-icon-letter']//li")).shouldHave(text("support@dartit.ru"))
        Selenide.`$`(By.id("ekaterinburg")).click()
        Selenide.`$`(By.xpath("//div[@id='city-ekaterinburg']//ul[@class='list-indent-icon  list-icon-point']//span")).shouldHave(text("ул. Малышева, 51, офис 1619"))
    }

    /*
    4.Проверить наличие и корректность адреса офиса в Екатеринбурге в “подвале” на всех страницах первого уровня навигации сайта.
    */
    @Test fun dartItCheckAddressAInFooter() {
        val menuItems = Selenide.`$$`(By.xpath("//header//div[@class='row']//ul[contains(@class,'menu')]//li")).shouldHave(size(7))
        menuItems.forEach()
        {
            it.click()
            println("Page title: ${Selenide.title()}")
            val address = Selenide.`$`(By.xpath("//a[@href='/contacts?ekaterinburg']"))
            address.shouldHave(text("Екатеринбург, ул. Малышева, 51, офис 1619, Бизнес центр \"Высоцкий\""))
            println("Address '${address.text}' on page '${Selenide.title()}' is okey")
        }
    }

    /*
    5.Проверить доступность для скачивания документа “Политика в отношении обработки персональных данных” на странице “О компании”
    */
    @Test fun dartItAboutCompanyTest() {
        Selenide.`$`(By.xpath("//span[text()='О компании']")).click()
        assertEquals("О компании",Selenide.title())
        val file = Selenide.`$`(By.xpath("//a[text()='Политика в отношении обработки персональных данных ООО \"ДартИТ\"']")).download()
        assertEquals("Politika_v_otnoshenii_obrabotki_personalnyih_dannyih_OOO__DartIT.docx",file.name);
    }
}