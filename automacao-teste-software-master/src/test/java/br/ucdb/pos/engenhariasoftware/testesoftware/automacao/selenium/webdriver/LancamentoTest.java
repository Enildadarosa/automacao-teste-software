package br.ucdb.pos.engenhariasoftware.testesoftware.automacao.selenium.webdriver;

import br.ucdb.pos.engenhariasoftware.testesoftware.automacao.selenium.webdriver.pageobject.LancamentoPage;
import br.ucdb.pos.engenhariasoftware.testesoftware.automacao.selenium.webdriver.pageobject.ListaLancamentosPage;
import br.ucdb.pos.engenhariasoftware.testesoftware.automacao.selenium.webdriver.pageobject.TipoLancamento;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.html.HTMLSelectElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class LancamentoTest {

    private WebDriver driver;
    private ListaLancamentosPage listaLancamentosPage;
    private LancamentoPage lancamentoPage;

    @BeforeClass
    private void inicialliza() {
        boolean windows = System.getProperty("os.name").toUpperCase().contains("WIN");
        System.setProperty("webdriver.gecko.driver",
                System.getProperty("user.dir") + "/src/test/resources/drivers/" +
                        "/geckodriver" + (windows ? ".exe" : ""));
        driver = new FirefoxDriver();
        listaLancamentosPage = new ListaLancamentosPage(driver);
        lancamentoPage = new LancamentoPage(driver);
    }

    @Test
    public void criaLancamento(){
        listaLancamentosPage.acessa();
        listaLancamentosPage.novoLancamento();

        LocalDateTime dataHoraBase = LocalDateTime.now();
        LocalDateTime dataHora = LocalDateTime.of(dataHoraBase.getYear(),
                dataHoraBase.getMonth() .getValue(), getDiaMes (), 0,0);
        DateTimeFormatter formatoLancamento = DateTimeFormatter.ofPattern("dd.MM.yy");
        final String descricaoLancamento = "Lançando saída automatizada " + dataHora.format(formatoLancamento);
        final BigDecimal valor = getValorLancamento();
        final TipoLancamento tipoLancamento = getTipoLancamento();
        final String categoria = getCategoria();

        lancamentoPage.cria(descricaoLancamento, valor, dataHora, tipoLancamento, categoria);
        WebElement descricao = driver.findElement(By.id("itemBusca"));
        descricao.click();
        descricao.sendKeys(descricaoLancamento);
        driver.findElement(By.id("bth-search")).click();

        new WebDriverWait(driver, 30)
                .until (ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tabelaLancamentos']/tbody/tr")));

        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, tipoLancamento, categoria));
    }

    @Test
    public void fluxo1 (){
        listaLancamentosPage.acessa();
        listaLancamentosPage.novoLancamento();

        LocalDateTime dataHora = LocalDateTime.now();
        DateTimeFormatter formatoLancamento = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        final String descricaoLancamento = "Lançamento de saída automatizada" + dataHora.format(formatoLancamento);
        final BigDecimal valor = getValorLancamento();
        final String categoria = getCategoria();
        lancamentoPage.cria(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA,
                categoria));
    }
    @Test
    public void fluxo2 (){
        listaLancamentosPage.acessa();
        listaLancamentosPage.novoLancamento();

        LocalDateTime dataHora = LocalDateTime.now();
        DateTimeFormatter formatoLancamento = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        final String descricaoLancamento = "Lançamento de saída automatizada" + dataHora.format(formatoLancamento);
        final BigDecimal valor = getValorLancamento();
        final String categoria = getCategoria();
        lancamentoPage.cria(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA,
                categoria));
        lancamentoPage.editaLancamento (descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        descricaoLancamento = "Lançamento editado" + dataHora.format(formatoLancamento);
        lancamentoPage.cria(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA,
                categoria));
    }
    @Test
    public void fluxo3 (){
        listaLancamentosPage.acessa();
        listaLancamentosPage.novoLancamento();

        LocalDateTime dataHora = LocalDateTime.now();
        DateTimeFormatter formatoLancamento = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        final String descricaoLancamento = "Lançamento de saída automatizada" + dataHora.format(formatoLancamento);
        final BigDecimal valor = getValorLancamento();
        final String categoria = getCategoria();
        lancamentoPage.cria(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA,
                categoria));
        lancamentoPage.editaLancamento (descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        descricaoLancamento = "Lançamento editado" + dataHora.format(formatoLancamento);
        lancamentoPage.cria(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA,
                categoria));
        lancamentoPage.excluirLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertFalse(lancamentoPage.excluirLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria););
   }
    @Test
    public void fluxo4 (){
        listaLancamentosPage.acessa();
        listaLancamentosPage.novoLancamento();

        LocalDateTime dataHora = LocalDateTime.now();
        DateTimeFormatter formatoLancamento = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        final String descricaoLancamento = "Lançamento de saída automatizada" + dataHora.format(formatoLancamento);
        final BigDecimal valor = getValorLancamento();
        final String categoria = getCategoria();
        lancamentoPage.cria(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA, categoria);
        assertTrue(listaLancamentosPage.existeLancamento(descricaoLancamento, valor, dataHora, TipoLancamento.SAIDA,
                categoria));
        HashMap<String, BigDecimal> totaisCalculados = lancamentoPage.calcularTotais();
        HashMap<String, BigDecimal> totaisObtidos = lancamentoPage.TotalEntradaSaida();

        boolean totaisEntradaIguais = totaisCalculados.get("total-entrada").compareTo(totaisObtidos.get("total-entrada")) == 0;
        boolean totaisSaidaIguais = totaisCalculados.get("total-saida").compareTo(totaisObtidos.get("total-saida")) == 0;

        assertTrue(totaisEntradaIguais, "Totais de entrada estão diferentes.");
        assertTrue(totaisSaidaIguais , "Totais de saida estão diferentes.");

        lancamentoPage.acessarRelatorios();

        assertTrue(listaLancamentosPage.acessouRelatorio(), "Falha aos acessar a tela de relatórios.");

    }

    @Test
    public void fluxo5(){

        listaLancamentosPage.acessa();
        listaLancamentosPage.novoLancamento();

        driver.findElement(By.id("btnSalvar")).click();

        assertTrue(lancamentoPage.validaAlerta(), "Não está validando os campos ou está faltando alguma validação.");

        driver.findElement(By.id("cancelar")).click();

        driver.findElement(By.id("recarregar")).click();

    }

    @AfterClass
    private void finaliza(){
        driver.quit();
    }

    public BigDecimal getValorLancamento() {

        boolean  aplicaVariante = (System.currentTimeMillis() % 3) == 0;
        int fator = 10;
        long mim = 30;
        long max = 900;
        if(aplicaVariante){
            mim /= fator;
            max /= fator;
        }
        return new BigDecimal(( 1 + (Math.random() * (max - mim)))).setScale(2, RoundingMode.HALF_DOWN);
    }
    private Integer getDiaMes (){

        return new Random().nextInt(27)+1;
    }

    private TipoLancamento getTipoLancamento (){
        Integer retorno;
        Random random = new Random();
        retorno = random.nextInt(1);

        if (retorno == 0)
            return TipoLancamento.ENTRADA;

        return TipoLancamento.SAIDA;
    }
    private String getCategoria(){
        Integer retorno;

        Random random = new Random();
        retorno = random.nextInt(7);

        if(retorno==0)
            return "ALIMENTACAO";
        else if(retorno==1)
            return "SALARIO";
        else if(retorno==2)
            return "LAZER";
        else if(retorno==3)
            return "TELEFONE_INTERNET";
        else if(retorno==4)
            return "CARRO";
        else if(retorno==5)
            return "EMPRESTIMO";
        else if(retorno==6)
            return "INVESTIMENTOS";
        return "OUTROS";

    }
    
}


