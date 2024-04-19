package com.tuhoc.dreamtunes.utils.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.ui.favorite.FavoriteFragment
import com.tuhoc.dreamtunes.ui.playlist.PlaylistFragment

class EditPlaylistDialog() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.edit_playlist_dialog, null)

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        val edtPlaylistName = view.findViewById<EditText>(R.id.edtPlaylistName)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnUpdate.setOnClickListener {
            val playlistName = edtPlaylistName.text.toString()
            if (playlistName.isNotEmpty()) {
                (parentFragment as? PlaylistFragment)?.updatePlaylist(playlistName)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập tên playlist", Toast.LENGTH_SHORT).show()
            }
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        return builder.create()
    }
}