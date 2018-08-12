package br.ucdb.pos.engenhariasoftware.testesoftware.automacao.selenium.webdriver.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LancamentoPage {

    private WebDriver driver;

    public LancamentoPage(final WebDriver driver){
        this.driver = driver;
    }

    public void cria(final String descricaoLancamento, final BigDecimal valorLancamento,
                     LocalDateTime dataHora, TipoLancamento tipo, String categoria){

        if(tipo == TipoLancamento.SAIDA) {
            driver.findElement(By.id("tipoLancamento2")).click(); // informa lançamento: SAÍDA
        }else{
            driver.findElement(By.id("tipoLancamento1")).click(); // informa lançamento: ENTRADA
        }

        WebElement descricao = driver.findElement(By.id("descricao"));
        descricao.click();
        descricao.sendKeys(descricaoLancamento);

        DateTimeFormatter formatoDataLancamento = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        WebElement dataLancamento = driver.findElement(By.name("dataLancamento"));
        dataLancamento.sendKeys(dataHora.format(formatoDataLancamento));

        WebElement categoriaelement = driver.findElement(By.id("categoria"));
        Select selectCategoria = new Select(categoriaelement);
        selectCategoria.selectByValue(categoria);

        WebElement valor = driver.findElement(By.id("valor"));
        driver.findElement(By.id("tipoLancamento2")).click();
        valor.sendKeys(String.valueOf(valorLancamento));
        driver.findElement(By.id("btnSalvar")).click();

    }
    private LancamentoRow buscarLancamentoNaTabela (LancamentoRow lancamento){
        WebElement tabela = driver.findElement(By.id("tabelaLancamentos"));
        WebElement tbody = tabela.findElement(By.tagName("tbody"));
        List<WebElement> linhas = tbody.findElements(By.tagName("tr"));
        LancamentoRow lancamentoRowBusca = null;

        for (WebElement linha : linhas){
            List<WebElement> colunas = linha.findElements(By.tagName("td"));
            LancamentoRow lancamentoRow = new LancamentoRow (colunas.get(0). getText(), colunas.get(1).getText(),
                    colunas.get(2).getText(), colunas.get(3).getText(), colunas.get(4).getText(), colunas.get(5).getText());
            if (comparaLancamento (LancamnetoRow, lancamento)){
                lancamentoRowBusca = lancamentoRow;
                break;
            }
        }
        return lancamentoRowBuca;
    }

    public void editaLancamento (String descricaoLancamento, BigDecimal valorLancamento, LocalDateTime dataHora,
                                 TipoLancamento tipoLancamento, String categoria){
    DateTimeFormatter formatoDataLancamento  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LancamentoRow lancamentoRowEncontrado = buscarLancamentoNaTabela (new LancamentoRow (descricaoLancamento,
            categoria.getTexto ()), dataHora.format(formatoDataLancamento),
            String.valueOf(valorLancamento).replaceAll("\\.", ","),
            tipoLancamento.getDescricao(),
            botoes: null));

        if(lancamentoRowEncontrado != null){
            WebElement div = lancamentoRowEncontrado.getBotoes().findElement(By.tagName("div"));
            List<WebElement> botoes = div.findElements(By.tagName("a"));
            botoes.get(0).click();
        }
    }
    public void excluirLancamento(String descricaoLancamento, BigDecimal valorLancamento,
                                  LocalDateTime dataHora, TipoLancamento tipo, String categoria){

        DateTimeFormatter formatoDataLancamento = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LancamentoRow lancamentoRowEncontrado = buscarLancamentoNaTabela(new LancamentoRow(descricaoLancamento,
                categoria.getTexto(),
                dataHora.format(formatoDataLancamento),
                String.valueOf(valorLancamento).replaceAll("\\.", ","),
                tipo.getDescricao(),
                null ));

        if(lancamentoRowEncontrado != null){
            WebElement div = lancamentoRowEncontrado.getBotoes().findElement(By.tagName("div"));
            List<WebElement> botoes = div.findElements(By.tagName("a"));
            botoes.get(1).click();
        }

    private boolean comparaLancamento(LancamentoRow lancamentoRow, LancamentoRow lancamento){

        if(lancamento.getDescricao().equals(lancamentoRow.getDescricao())
                && lancamento.getDataLancamento().equals(lancamentoRow.getDataLancamento())
                && lancamento.getCategoria().equals(lancamentoRow.getCategoria())
                && lancamento.getTipo().equals(lancamentoRow.getTipo())
                && lancamento.getValor().equals(lancamentoRow.getValor())){
            return true;
        }

        return false;

    }
        public categoria obterOptionParaSelecionar(){

            WebElement categoria = driver.findElement(By.id("categoria"));
            Select selectCategoria = new Select(categoria);
            List<WebElement> options = selectCategoria.getOptions();

            Random random = new Random();
            int tamanho = 0;
            int posicao = 0;
            if(options.size() > 0){
                if(options.get(0).getText().isEmpty()){
                    tamanho = options.size() - 1;
                    posicao = random.nextInt(tamanho) + 1;
                    return new categoria(posicao, options.get(posicao).getText());
                }else{
                    posicao = random.nextInt();
                    return new categoria(posicao, options.get(posicao).getText());
                }
            }
            return null;

        }

        public void selecionarCategoria(int posicao){

            WebElement categoria = driver.findElement(By.id("categoria"));
            Select selectCategoria = new Select(categoria);
            List<WebElement> options = selectCategoria.getOptions();
            options.get(posicao).click();

        }
        public HashMap<String, BigDecimal> totalEntradaSaida(){

            HashMap<String, BigDecimal> totais = new HashMap<>();
            BigDecimal totalEntrada = new BigDecimal(0);
            BigDecimal totalSaida = new BigDecimal(0);

            WebElement tabela = driver.findElement(By.id("tabelaLancamentos"));
            WebElement tfoot = tabela.findElement(By.tagName("tfoot"));
            List<WebElement> linhas = tfoot.findElements(By.tagName("tr"));

            if(linhas.size() == 2){

                String valorSaida = linhas.get(0).findElement(By.tagName("th"))
                        .findElement(By.tagName("span")).getText();

                String valorEntrada = linhas.get(1).findElement(By.tagName("th"))
                        .findElement(By.tagName("span")).getText();

                valorSaida = valorSaida.replaceAll("\\.","");
                valorEntrada = valorEntrada.replaceAll("\\.","");

                totalSaida = new BigDecimal(valorSaida
                        .replaceAll(",","."));

                totalEntrada = new BigDecimal(valorEntrada
                        .replaceAll(",","."));

            }

            totais.put("total-entrada", totalEntrada);
            totais.put("total-saida", totalSaida);

            return totais;
        }

        public HashMap<String, BigDecimal> calcularTotais(){

            HashMap<String, BigDecimal> totais = new HashMap<>();

            BigDecimal totalEntrada = new BigDecimal(0);
            BigDecimal totalSaida = new BigDecimal(0);

            WebElement tabela = driver.findElement(By.id("tabelaLancamentos"));
            WebElement tbody = tabela.findElement(By.tagName("tbody"));
            List<WebElement> linhas = tbody.findElements(By.tagName("tr"));

            for(WebElement linha : linhas){

                List<WebElement> colunas = linha.findElements(By.tagName("td"));
                LancamentoRow lancamentoRow = new LancamentoRow(colunas.get(0).getText(), colunas.get(1).getText(),colunas.get(2).getText(),
                        colunas.get(3).getText(), colunas.get(4).getText(), colunas.get(5));

                if(lancamentoRow.getTipo().equals(TipoLancamento.SAIDA.getDescricao())){
                    totalSaida = totalSaida.add(new BigDecimal(lancamentoRow.getValor().replaceAll(",", ".")));
                }else if(lancamentoRow.getTipo().equals(TipoLancamento.ENTRADA.getDescricao())){
                    totalEntrada = totalEntrada.add(new BigDecimal(lancamentoRow.getValor().replaceAll(",", ".")));
                }

            }

            totais.put("total-entrada", totalEntrada);
            totais.put("total-saida", totalSaida);

            return totais;

        }

        public void acessarRelatorios(){

            List<WebElement> links = driver.findElements(By.tagName("a"));
            WebElement linkEncontrado = null;
            for(WebElement link : links){
                if(link.getAttribute("title").equals("Gráfico")){
                    linkEncontrado = link;
                }
            }

            if(linkEncontrado != null){
                linkEncontrado.click();
            }
        }

        public boolean validaAlerta(){
            //  driver.findElement(By.cssSelector("alert"));
            List<WebElement> alertas = driver.findElement(By.className("alert")).findElements(By.tagName("div"));
            if(alertas.size() > 0){

                boolean existeMsgDescricao = false;
                boolean existeMsgData = false;
                boolean existeMsgValor = false;
                boolean existeMsgCategoria = false;

                for(WebElement alerta : alertas){
                    if(alerta.findElement(By.tagName("span"))
                            .getText().equals("A descrição deve ser informada")){
                        existeMsgDescricao = true;
                    }else if(alerta.findElement(By.tagName("span"))
                            .getText().equals("A data deve ser informada")){
                        existeMsgData = true;
                    }else if(alerta.findElement(By.tagName("span"))
                            .getText().equals("O valor deve ser informado")){
                        existeMsgValor = true;
                    }else if(alerta.findElement(By.tagName("span"))
                            .getText().equals("A categoria deve ser informada")){
                        existeMsgCategoria = true;
                    }
                }

                if(existeMsgCategoria && existeMsgData && existeMsgDescricao && existeMsgValor){
                    return true;
                }
            }

            return false;
        }

}


