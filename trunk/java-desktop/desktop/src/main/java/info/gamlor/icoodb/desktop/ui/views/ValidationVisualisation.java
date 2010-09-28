package info.gamlor.icoodb.desktop.ui.views;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;

/**
 * @author roman.stoffel@gamlor.info
 * @since 25.07.2010
 */
public final class ValidationVisualisation {

    public static void installValidationVisualisation(BindingGroup binding){
        binding.addBindingListener(new ValidatorBindingListener());
        forceValidation(binding);
    }


    private static void forceValidation(BindingGroup bindingGroup){
        //Need to be done after initialization to run the validators!
        for(Binding b : bindingGroup.getBindings()){
            b.saveAndNotify();
        }
    }
}
