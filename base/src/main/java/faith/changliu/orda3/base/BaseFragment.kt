package faith.changliu.orda3.base

import android.support.v4.app.Fragment

open class BaseFragment : Fragment() {
	
	val mLoading by lazy { (activity as? BaseActivity)?.mLoading }
	
	
}