package faith.changliu.orda3.base.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.RequestApplication
import kotlinx.android.synthetic.main.cell_application.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlin.properties.Delegates

class ApplicationsAdapter(
		var requests: ArrayList<RequestApplication>,
		private val onAssign: (RequestApplication) -> Unit
) : RecyclerView.Adapter<ApplicationsAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_application, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return requests.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(requests[position])
	}

	inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

		private var isOpen by Delegates.observable(false) { _, _, newValue ->
			itemView.mLayoutReveal.visibility = if (newValue) View.VISIBLE else View.GONE
		}

		fun bind(request: RequestApplication) {

			launch(UI) {
				val user = async(CommonPool) {
					FireDB.readUserWithId(request.appliedBy)
				}.await()

				val ratings = async(CommonPool) {
					FireDB.readAllRatingsForTravelerId(user.id)
				}.await()

				var total = 0.0
				ratings.map { total += it.rate }
				val avgRating = total / ratings.size

				itemView.apply {
					mTvName.text = user.name
					mTvRating.text = avgRating.toString()

					mBtnUpdateRequest.setOnClickListener {
						// todo
						onAssign(request)
						isOpen = false
					}
					mBtnDeleteRequest.setOnClickListener {
						// todo
						isOpen = false
					}

					mCellView.setOnClickListener {
						isOpen = !isOpen
					}
				}

			}


		}
	}
}