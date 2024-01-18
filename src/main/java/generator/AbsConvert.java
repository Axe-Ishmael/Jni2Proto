package generator;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;

public abstract class AbsConvert<T> {

    private Template template;

    public AbsConvert() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(this.getClass(), "/");
        try {
            template = configuration.getTemplate(getFtlName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convert(T src) {
        StringWriter outWriter = new StringWriter();
        try {
            template.process(src, outWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outWriter.toString();
    }

    public abstract String getFtlName();
}