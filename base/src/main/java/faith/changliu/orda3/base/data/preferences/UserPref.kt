package faith.changliu.orda3.base.data.preferences

import android.content.Context
import android.content.SharedPreferences
import faith.changliu.orda3.base.AppContext
import faith.changliu.orda3.base.data.models.User
import kotlin.properties.Delegates

object UserPref {
	
	private val mSp: SharedPreferences by lazy { AppContext.getSharedPreferences("user_pref", Context.MODE_PRIVATE) }
	var mUser: User by Delegates.observable(User()) { property, oldValue, newValue ->
		setId(newValue.id)
		setEmail(newValue.email)
	}
	
	const val PKG_NAME = "faith.changliu.base"
	const val USER_ID = "$PKG_NAME.id"
	const val USER_EMAIL = "$PKG_NAME.email"

	fun getId() = mSp.getString(USER_ID, "")
	fun setId(userId: String) {
		mSp.edit { putString(USER_ID, userId) }
	}

	fun getEmail() = mSp.getString(USER_EMAIL, "")
	fun setEmail(email: String) {
		mSp.edit { putString(USER_EMAIL, email) }
	}
	
}

inline fun SharedPreferences.edit(f: SharedPreferences.Editor.() -> Unit) {
//	Date()
	val editor = edit()
	editor.f()
	editor.apply()
}