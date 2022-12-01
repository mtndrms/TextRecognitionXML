package com.example.textrecognitionxml.utils

import com.example.textrecognitionxml.models.Document
import com.example.textrecognitionxml.models.Folder
import java.util.*

object FakeDatabase {
    private val documents: List<Document> = listOf(
        Document(
            0,
            0,
            "First Title",
            "A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            1,
            0,
            "Nunquam gratia extum.",
            "Cum candidatus manducare, omnes solemes prensionem noster, dexter buboes.",
            Calendar.getInstance().time
        ),
        Document(
            2,
            0,
            "Fidelis manifestums torquis est.",
            "Vae. Brevis rector velox desideriums urbs est. Heu. Experimentum velox ducunt ad dexter ionicis tormento.",
            Calendar.getInstance().time
        ),
        Document(
            3,
            1,
            "Cum, dexter clabularees.",
            "Agripeta manducares, tanquam domesticus domus. Persuadere solite ducunt ad flavum rector. Est teres palus, cesaris.",
            Calendar.getInstance().time
        ),
        Document(
            4,
            1,
            "Demissios mori in oenipons!",
            "Fides grandis orgia est. A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            5,
            1,
            "Frondators manducare!",
            "Pars velums, tanquam peritus ratione. A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            6,
            2,
            "Heu, poeta!",
            "Tumultumques credere in piscinam! A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            7,
            2,
            "Azureus, de brevis, ferox accentor.",
            "Ecce. A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            8,
            2,
            "Ubi est brevis orgia?",
            "Historias ire! A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            9,
            2,
            "Rusticus, festus toruss.",
            "Tuss potus in varius gandavum! A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
        Document(
            10,
            2,
            "Nunquam captis barcas.",
            "Cum nuclear vexatum iacere volare, omnes turpises gratia germanus, alter ionicis tormentoes. A falsis, nixus varius terror. Cum zirbus accelerare, omnes aususes anhelare camerarius, azureus galataees. Nomen de brevis equiso, manifestum calcaria!",
            Calendar.getInstance().time
        ),
    )

    val folders: List<Folder> = listOf(
        Folder(0, "Notes for Work"),
        Folder(1, "Book Quotes"),
        Folder(2, "Education")
    )

    fun generateFolderAndItsDocuments() {
        documents.forEach {
            folders[it.folderId].run {
                documents.add(it)
            }
        }
    }
}