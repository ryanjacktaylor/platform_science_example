package com.duckmethodsw.platformscience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duckmethodsw.platformscience.models.AssignmentData
import com.duckmethodsw.platformscience.services.AssigmentService
import com.duckmethodsw.platformscience.services.AssignmentDataService
import com.duckmethodsw.platformscience.ui.AssignmentsAdapter
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private lateinit var recyclerView: RecyclerView
    private lateinit var assignmentsAdapter: AssignmentsAdapter
    private lateinit var assignmentService: AssigmentService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the recylcerview/adapter
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        assignmentsAdapter = AssignmentsAdapter()
        recyclerView.adapter = assignmentsAdapter

        /*
        Get the data. Normally, this would be an async call to a backend and we'd handle this
        with a coroutine or rx call. Once the data is returned, we'd call assignment service to
        create the assignments. This may also take a significant amount of time, so it probably
        would make sense
         */
        val assignmentData : AssignmentData = AssignmentDataService().getAssignmentData()

        assignmentService = AssigmentService(assignmentData)
        assignmentsAdapter.bindData(assignmentService.getAssignments())
    }

    override fun onStart() {
        super.onStart()
        fetchAssignmentData()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun fetchAssignmentData(){
        /* So this is unnecessary in this example due to the small data set. Normally fetching data
        would be an async call to the back end, but in this case it's a quick parsing. Likewise, the
        call to the assignment service could be quite lengthy, and we wouldn't want to block the UI
        thread or cause ANR's. But in this case, the data set is quite small and probably won't cause
        an issue.
        */
        coroutineScope.launch {
            val assignmentData : AssignmentData = AssignmentDataService().getAssignmentData()
            assignmentService = AssigmentService(assignmentData)
            assignmentsAdapter.bindData(assignmentService.getAssignments())
        }
    }

}