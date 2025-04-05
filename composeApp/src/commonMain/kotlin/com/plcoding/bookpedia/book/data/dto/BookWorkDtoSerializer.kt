package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object BookWorkDtoSerializer: KSerializer<BookWorkDto> {

    //JSON’un nasıl bir yapıya sahip olduğunu tanımlayan bir şema gibidir.
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        serialName = BookWorkDto::class.simpleName!!,// DTO yapısını tanımlar
    ){

        element<String?>("description") // JSON’daki “description” alanını tanımlıyoruz.
    }


    // API sacma oldugu icin description 2 şekilde gelebiliyor
    // 1- Json içinde Description objesi var içerisinde ki value desctiption
    // 2- Json içinde description stringi var

    //→ JSON’dan BookWorkDto nesnesi üretir.
    override fun deserialize(decoder: Decoder): BookWorkDto = decoder.decodeStructure(descriptor) {

       // 🔹 decoder.decodeStructure(descriptor) → JSON’un içini adım adım okumamızı sağlar.
        var description: String? = null // → JSON'da olmayabilir veya null olabilir

        while (true){//→ JSON içindeki tüm alanları teker teker okuyana kadar döngüde kalırız.

            when(val index = decodeElementIndex(descriptor)){ // index decriptorda tanımladıgımız element sıralmasını yansıtır json sıralmasını değil

                //JSON formatında key-value mantığı vardır ve key'lerin sırası önemli değildir.
                // descriptor içindeki sıralama o yüzden 0 neye denk geliyor biliyoruz. descriptionun jsonda kaçıncı sırada oldugunun onemı yok
                0 -> {  // description
                    //Kotlinx Serialization framework'ünden decoder geliyor herzaman Json olmayabilir
                    val jsonDecoder = decoder as? JsonDecoder?: throw SerializationException("Expected Json Decoder")
                    val element = jsonDecoder.decodeJsonElement()
                    description =
                        //CASE 1 Description Json'da obje olarak tanımlanmış ve içindeki value kısmına bilgi girilmiş
                        if (element is JsonObject){

                            decoder.json.decodeFromJsonElement<DescriptionDto>(
                                element = element,
                                deserializer = DescriptionDto.serializer()
                            ).value
                        } //CASE 2 Desctiption direkt bir satırda string olarak verilmiş
                        else if (element is JsonPrimitive && element.isString){
                            element.content
                        }else { null }

                }
                CompositeDecoder.DECODE_DONE -> break // tüm indexler bitince
                else -> { // bizim için sadece description önemli
                    throw SerializationException("Unexpected index $index")
                }
            }
        }
        return@decodeStructure BookWorkDto(description ?: "")
    }

    // dto to json.
    override fun serialize(encoder: Encoder, value: BookWorkDto) {
        // JSON formatında encode işlemi yapacağımız için Encoder'ı JsonEncoder'a çevirmemiz gerek
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("Expected Json Encoder")

        // JSON nesnesini oluşturuyoruz
        val jsonObject = buildJsonObject {
            value.description?.let { description ->
                put("description", description)
            }
        }

        // JSON nesnesini encode ediyoruz
        jsonEncoder.encodeJsonElement(jsonObject)
    }


}