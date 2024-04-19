package com.tuhoc.dreamtunes.utils.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.ui.playlist.PlaylistFragment

class DeletePlaylistDialog() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.delete_playlist_dialog, null)

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnDelete.setOnClickListener {
            (parentFragment as? PlaylistFragment)?.deletePlaylist()
            dismiss()
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        return builder.create()
    }
}