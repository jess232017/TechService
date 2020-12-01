package com.koopers.techservice

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.wajahatkarim3.easyvalidation.core.view_ktx.*
import es.dmoral.toasty.Toasty

class Stringer(private var cxt: Context) {

    fun valName(Name: EditText):String{
        if(!Name.minLength(5)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_name_largo), Toasty.LENGTH_SHORT,true).show()
            Name.requestFocus()
            return ""
        }

        if(!Name.maxLength(50)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_name_corto), Toasty.LENGTH_SHORT, true).show()
            Name.requestFocus()
            return ""
        }

        return Name.text.toString().trim()
    }

    fun valMail(Mail: EditText):String{
        if(!Mail.validEmail()){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_mail_valido), Toasty.LENGTH_SHORT, true).show()
            Mail.requestFocus()
            return ""
        }

        if(!Mail.minLength(10)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_mail_largo), Toasty.LENGTH_SHORT, true).show()
            Mail.requestFocus()
            return ""
        }

        if(!Mail.maxLength(40)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_mail_corto), Toasty.LENGTH_SHORT,true).show()
            Mail.requestFocus()
            return ""
        }

        return Mail.text.toString().trim()
    }

    fun valPass(Pass: EditText):String{
        if(!Pass.minLength(5)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_pass_largo), Toast.LENGTH_SHORT, true).show()
            Pass.requestFocus()
            return ""
        }

        if(!Pass.maxLength(20)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_pass_corto), Toast.LENGTH_SHORT, true).show()
            Pass.requestFocus()
            return ""
        }

        /*if(!Pass.atleastOneNumber()){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_pass_digit), Toast.LENGTH_SHORT, true).show()
            Pass.requestFocus()
            return ""
        }

        if(!Pass.atleastOneSpecialCharacters()){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_pass_special), Toast.LENGTH_SHORT, true).show()
            Pass.requestFocus()
            return ""
        }

        if(!Pass.atleastOneUpperCase()){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_pass_upper), Toast.LENGTH_SHORT, true).show()
            Pass.requestFocus()
            return ""
        }*/

        return Pass.text.toString().trim()
    }

    fun valText(Text: EditText, min: Int, max: Int):String{
        if(!Text.minLength(min)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_text_corto), Toast.LENGTH_SHORT, true).show()
            Text.requestFocus()
            return ""
        }

        if(!Text.maxLength(max)){
            Toasty.warning(cxt, cxt.getString(R.string.escribe_text_largo), Toast.LENGTH_SHORT, true).show()
            Text.requestFocus()
            return ""
        }

        return Text.text.toString().trim()
    }
}